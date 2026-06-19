package br.com.petsaude.regula_ai_backend;

import br.com.petsaude.regula_ai_backend.Service.ImportacaoFilaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImportacaoRunner {

    private static final Logger log = LoggerFactory.getLogger(ImportacaoRunner.class);
    private static final int CHUNK_SIZE = 500;

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Uso: java -jar regula-ai-backend.jar <caminho-do-csv>");
            System.exit(1);
        }

        Path arquivo = Path.of(args[0]);
        if (!Files.exists(arquivo)) {
            System.err.println("Arquivo não encontrado: " + arquivo.toAbsolutePath());
            System.exit(1);
        }

        SpringApplication app = new SpringApplication(RegulaAiBackendApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);

        try (ConfigurableApplicationContext ctx = app.run()) {
            ImportacaoFilaService service = ctx.getBean(ImportacaoFilaService.class);
            executar(service, arquivo);
        }
    }

    private static void executar(ImportacaoFilaService service, Path arquivo) throws Exception {
        log.info("========================================");
        log.info("   IMPORTAÇÃO DE ENCAMINHAMENTOS");
        log.info("   Arquivo: {}", arquivo.toAbsolutePath());
        log.info("========================================");

        long inicio = System.currentTimeMillis();

        // Fase 1: pré-carga
        log.info("[1/3] Pré-carregando lookups do banco...");
        ImportacaoFilaService.LookupContext lookups = service.precarregarLookups();

        // Fase 2: parsing do CSV
        log.info("[2/3] Lendo e parseando CSV...");
        List<Map<String, String>> linhas = service.parsearCsv(arquivo);
        log.info("      {} linhas de dados encontradas.", linhas.size());

        // Fase 3: processamento em chunks
        int totalChunks = (linhas.size() + CHUNK_SIZE - 1) / CHUNK_SIZE;
        log.info("[3/3] Processando em {} chunks de {} linhas...", totalChunks, CHUNK_SIZE);

        int totalCriados = 0, totalAtualizados = 0, totalErros = 0;
        List<String> todosErros = new ArrayList<>();

        for (int i = 0; i < linhas.size(); i += CHUNK_SIZE) {
            int chunkNum = (i / CHUNK_SIZE) + 1;
            int fim = Math.min(i + CHUNK_SIZE, linhas.size());
            List<Map<String, String>> chunk = linhas.subList(i, fim);

            ImportacaoFilaService.ChunkResultado resultado = service.processarChunk(chunk, lookups);

            totalCriados += resultado.criados();
            totalAtualizados += resultado.atualizados();
            totalErros += resultado.erros().size();
            if (todosErros.size() < 100) {
                todosErros.addAll(resultado.erros());
            }

            log.info("  Chunk {}/{} | linhas {}-{} | +{} criados | ~{} atualizados | {} erros",
                chunkNum, totalChunks, i + 1, fim,
                resultado.criados(), resultado.atualizados(), resultado.erros().size());
        }

        long duracaoSeg = (System.currentTimeMillis() - inicio) / 1000;

        log.info("========================================");
        log.info("   IMPORTAÇÃO CONCLUÍDA");
        log.info("   Tempo total : {}s", duracaoSeg);
        log.info("   Total linhas: {}", linhas.size());
        log.info("   Criados     : {}", totalCriados);
        log.info("   Atualizados : {}", totalAtualizados);
        log.info("   Erros       : {}", totalErros);
        log.info("========================================");

        if (!todosErros.isEmpty()) {
            log.warn("Primeiros erros registrados:");
            todosErros.stream().limit(20).forEach(e -> log.warn("  - {}", e));
        }
    }
}
