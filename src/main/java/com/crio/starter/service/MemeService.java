package com.crio.starter.service;


import com.crio.starter.data.MemeEntity;
import com.crio.starter.exception.DuplicateMemeException;
import com.crio.starter.exchange.MemeDto;
import com.crio.starter.repository.MemeRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MemeService {
  @Autowired
  private MemeRepository memeRepository;

  public MemeEntity saveMeme(MemeDto memeDto) {

    try {
      if (memeRepository.existsByName(memeDto.getName())) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Meme with the same name already exists");
      }
      MemeEntity meme = new MemeEntity();
      meme.setName(memeDto.getName());
      meme.setUrl(memeDto.getUrl());
      meme.setCaption(memeDto.getCaption());
      return memeRepository.save(meme);
    } catch (DuplicateKeyException e) {
      throw new DuplicateMemeException("Meme already exists");
    }

  }

  // public List<MemeDto> getLatestMemes() {
  // return memeRepository.findAll().stream()
  // .sorted((m1, m2) -> m2.getId().compareTo(m1.getId())).limit(100).map(meme -> {
  // MemeDto memeDTO = new MemeDto();
  // memeDTO.setName(meme.getName());
  // memeDTO.setUrl(meme.getUrl());
  // memeDTO.setCaption(meme.getCaption());
  // return memeDTO;
  // }).collect(Collectors.toList());
  // }

  public List<MemeEntity> getLatestMemes() {
    return memeRepository.findAll(PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "id")))
        .getContent();
  }

  public MemeDto getMemeById(String id) {
    return memeRepository.findById(id).map(meme -> {
      MemeDto memeDto = new MemeDto();
      memeDto.setName(meme.getName());
      memeDto.setUrl(meme.getUrl());
      memeDto.setCaption(meme.getCaption());
      return memeDto;
    }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Meme not found"));
  }
}
