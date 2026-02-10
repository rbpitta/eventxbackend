package com.events.eventxx.service;

import com.events.eventxx.model.Companion;
import com.events.eventxx.model.Guest;
import com.events.eventxx.repository.GuestRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ReportGuestService {
    @Autowired
    private GuestRepository guestRepository; // Supondo que você tenha um repositório para Guest

    public void generateReport(String codEvent, HttpServletResponse response, Pageable pageable) throws IOException {
        // Cria uma nova planilha
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Relatório de Convidados");

        // Cabeçalhos
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Nome do Convidado");
        headerRow.createCell(1).setCellValue("Presença do Convidado");
        headerRow.createCell(2).setCellValue("Data de Atualização do Convidado");
        headerRow.createCell(3).setCellValue("Acompanhantes");
        headerRow.createCell(4).setCellValue("Presença dos Acompanhantes");
        headerRow.createCell(5).setCellValue("Data de Atualização dos Acompanhantes");
        headerRow.createCell(5).setCellValue("titulo");

        // Busca todos os convidados
        Page<Guest> guests = guestRepository.findByEvent_CodEvent(codEvent, pageable);
        int totalPresent = 0;

        // Preenche a planilha com dados dos convidados
        int rowNum = 1;
        for (Guest guest : guests) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(guest.getNome());
            row.createCell(1).setCellValue("presente".equalsIgnoreCase(guest.getPresent()) ? 1 : 0);
            row.createCell(2).setCellValue(guest.getUpdatedAt().toString()); // Adiciona a data de atualização do convidado

            // Acompanhantes
            StringBuilder companions = new StringBuilder();
            StringBuilder companionsPresence = new StringBuilder();
            StringBuilder companionsUpdateDate = new StringBuilder();
            for (Companion companion : guest.getCompanionList()) {
                companions.append(companion.getNome()).append(", ");
                companionsPresence.append("presente".equalsIgnoreCase(companion.getPresent()) ? 1 : 0).append(", ");
                companionsUpdateDate.append(companion.getUpdatedAt().toString()).append(", ");
            }

            if (companions.length() > 0) {
                companions.setLength(companions.length() - 2);
            }

            if (companionsPresence.length() > 0) {
                companionsPresence.setLength(companionsPresence.length() - 2);
            }

            if (companionsUpdateDate.length() > 0) {
                companionsUpdateDate.setLength(companionsUpdateDate.length() - 2);
            }

            row.createCell(3).setCellValue(companions.toString());
            row.createCell(4).setCellValue(companionsPresence.toString());
            row.createCell(5).setCellValue(companionsUpdateDate.toString());

            // Contabiliza presença
            if ("presente".equalsIgnoreCase(guest.getPresent())) {
                totalPresent++;
            }
            // Contabiliza presença dos acompanhantes
            for (Companion companion : guest.getCompanionList()) {
                if ("presente".equalsIgnoreCase(companion.getPresent())) {
                    totalPresent++;
                }
            }
        }

        // Adiciona a quantidade total de pessoas presentes
        Row totalRow = sheet.createRow(rowNum);
        totalRow.createCell(0).setCellValue("Total de Pessoas Presentes:");
        totalRow.createCell(1).setCellValue(totalPresent);
        // Salva o arquivo
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=relatorio.xlsx");
            workbook.write(response.getOutputStream());
        } finally {
            workbook.close();
        }
    }
}

