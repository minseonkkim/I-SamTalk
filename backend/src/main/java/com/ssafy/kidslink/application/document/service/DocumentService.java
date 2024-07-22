package com.ssafy.kidslink.application.document.service;

import com.ssafy.kidslink.application.document.domain.Absent;
import com.ssafy.kidslink.application.document.domain.Dosage;
import com.ssafy.kidslink.application.document.dto.DocumentDTO;
import com.ssafy.kidslink.application.document.mapper.AbsentMapper;
import com.ssafy.kidslink.application.document.mapper.DosageMapper;
import com.ssafy.kidslink.application.document.repository.AbsentRepository;
import com.ssafy.kidslink.application.document.repository.DosageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final AbsentRepository absentRepository;
    private final DosageRepository dosageRepository;
    private final AbsentMapper absentMapper;
    private final DosageMapper dosageMapper;

    public List<DocumentDTO> getAllDocuments() {
        List<Absent> absents = absentRepository.findAll();
        List<Dosage> dosages = dosageRepository.findAll();

        List<DocumentDTO> allDocuments = new ArrayList<>();

        for (Absent absent : absents) {
            DocumentDTO document = new DocumentDTO();
            document.setDate(absent.getAbsentStartdate());
            document.setType("Absent");
            document.setAbsent(absentMapper.toDTO(absent));
            allDocuments.add(document);
        }

        for (Dosage dosage : dosages) {
            DocumentDTO document = new DocumentDTO();
            document.setDate(dosage.getDosageStartdate());
            document.setType("Dosage");
            document.setDosage(dosageMapper.toDTO(dosage));
            allDocuments.add(document);
        }

        Collections.sort(allDocuments, (d1, d2) -> d1.getDate().compareTo(d2.getDate()));
        return allDocuments;
    }
}