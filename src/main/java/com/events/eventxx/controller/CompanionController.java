package com.events.eventxx.controller;

import com.events.eventxx.model.Companion;
import com.events.eventxx.repository.CompanionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController("/api")
public class CompanionController {
    @Autowired
    CompanionRepository companionRepository;

    @PostMapping("/companion")
    public ResponseEntity saveCompanion(@RequestBody Companion companion) {
        try {
            companionRepository.save(companion);
            return ResponseEntity.ok("Companion saved successfully");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/companion")
    public ResponseEntity deleteCompanion(@RequestBody Integer id) {
        Optional<Companion> companion = companionRepository.findById(id);
        if (companion.isPresent()) {
            companionRepository.delete(companion.get());
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    //TODO *EDIT DO GUEST NO MODAL FICAR DENTRO DE UM FORM
    //TODO *RELOAD NA TELA QUANDO ANEXA O EXCEL--OK
    //TODO MSG QUANDO NÃO TEM LISTA DE EVENTO AINDA E A TABELA ESTÁ VAZIA
    //TODO PAGINACAO
    //TODO BUSCA
    // TODO CONTAINER NA TELAS
    //TODO COR BOTÃO
    //TODO HEADER E FOOTER
    //TODO BOTÃO DE MOSTRAR QR CODE PARA GUEST E COMPANION
    //TODO TABELA APAGA OS DADOS QUANDO O SISTEMA STARTA
    //TODO CONFERIR PLANILHA EXCEL
    //TODO ADD TIPO DE EVENTO (TALVEZ NÃO SEJA NECESSÁRIO)
    //TODO MODAL ADD CONVIDADO
    //TODO ERRO NO BACK AO SUBIR PROJETO

    //TODO QR
    //TODO GERAR PELO ID DA TABELA + CHAVA ALEATORIA
    //TODO SALVAR NA COLUNA DO BANCO
    //TODO ENVIAR POR EMAIL
    //TODO QUANDO CLICAR EM VER DEVOLVER PARA TELA O QR ABERTO EM NOVA ABA
    //TODO VALIDAR COM SUCESSO COM MODAL
    //TODO DANDO SUCESSO SALVAR OK NA BASE DE USADA

}
