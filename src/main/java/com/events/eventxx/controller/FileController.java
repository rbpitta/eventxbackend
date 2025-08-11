package com.events.eventxx.controller;

import com.events.eventxx.Dto.CompanionDTO;
import com.events.eventxx.model.Event;
import com.events.eventxx.repository.EventRepository;
import com.events.eventxx.service.EventService;
import com.events.eventxx.service.GuestService;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    GuestService guestService;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventService eventService;
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @PostMapping("/upload/{codEvent}")
    public ResponseEntity processFileExcel(@RequestParam("file") MultipartFile file,
        @PathVariable String codEvent) {

        Event event = eventRepository.findByCodEvent(codEvent);

        if(event == null){
            return ResponseEntity.notFound().build();

        }
        Map<String,List<CompanionDTO>> mapGuest = new HashMap<>();

        try (InputStream inputStream = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet firstTab = workbook.getSheetAt(0);
            boolean isFirstLine = true;

            for (Row line : firstTab) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                processLine(line, mapGuest);
            }

            guestService.saveGuest(mapGuest, event);

            logger.info("Arquivo processado com sucesso. Total de convidados: {}", mapGuest.size());

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            logger.error("Erro ao processar o arquivo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao processar o arquivo: " + e.getMessage());
        }
    }

    private void processLine(Row line, Map<String, List<CompanionDTO>> mapGuest) {
        Cell nameCell = line.getCell(8); // Convidado principal
        Cell companionCell = line.getCell(7); // Acompanhantes
        Cell emailCell = line.getCell(10); // E-mails

        if (nameCell == null || emailCell == null) return;
        if (nameCell.getStringCellValue().trim().isEmpty() || emailCell.getStringCellValue().trim().isEmpty()) return;


        String guestName = nameCell.getStringCellValue().trim();
        String[] companionNames = companionCell != null ? splitData(companionCell.getStringCellValue()) : new String[0];
        String[] emails = splitData(emailCell.getStringCellValue());

        List<CompanionDTO> companionList = new ArrayList<>();

        // Convidado principal
        String guestEmail = emails.length > 0 ? emails[0].trim() : "";
        companionList.add(new CompanionDTO("", guestEmail)); // "" = nome do convidado já está como chave

        // Acompanhantes
        for (int i = 0; i < companionNames.length; i++) {
            String name = companionNames[i].trim();
            String email = (i + 1) < emails.length ? emails[i + 1].trim() : "";
            companionList.add(new CompanionDTO(name, email));
        }

        mapGuest.put(guestName, companionList);
    }


    private String[] splitData(String valor) {
        return valor.split("\\s*,\\s*");
    }


    //FALTA SETAR AS INFORMAÇÕES PARA QUANDO OCORRER FALHAS
    //FORMATAR MSG EMAIL
    //VER AS THREADS
    //LER QR CODE E DAR BAIXA NA TABELA--*


    //ABRIR QR CODE EM UM MODAL--* não precisa pois a propia app não vai ler ela mesmo é só add uma nova pessoa


    //FORMATAR AS TELAS- ARRUMAR MODAIS, COLOCAR CONTAINER, FOOTER E HEADER E FOTO
    //ARRUMAR AS URLS DA SERVICE
}

