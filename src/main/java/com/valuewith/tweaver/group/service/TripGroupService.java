package com.valuewith.tweaver.group.service;

import com.valuewith.tweaver.constants.GroupStatus;
import com.valuewith.tweaver.constants.ImageType;
import com.valuewith.tweaver.defaultImage.entity.DefaultImage;
import com.valuewith.tweaver.defaultImage.repository.DefaultImageRepository;
import com.valuewith.tweaver.defaultImage.service.ImageService;
import com.valuewith.tweaver.group.dto.TripGroupRequestDto;
import com.valuewith.tweaver.group.entity.TripGroup;
import com.valuewith.tweaver.group.repository.TripGroupRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TripGroupService {
  private final ImageService imageService;

  private final TripGroupRepository tripGroupRepository;
  private final DefaultImageRepository defaultImageRepository;

  public TripGroup createTripGroup(TripGroupRequestDto tripGroupRequestDto, MultipartFile file) {

    if (file != null && !file.isEmpty()) {
      String imageUrl = imageService.uploadImageAndGetUrl(file, ImageType.THUMBNAIL);
      tripGroupRequestDto.setThumbnailUrl(imageUrl);
    } else {
      // 사용자로 부터 사진 등록을 받지 못한 경우
      // 입력받은 지역으로 default_image에 있는 사진중에 랜덤 한장 뽑아서 저장
      tripGroupRequestDto.setThumbnailUrl(getThumbnailUrl(tripGroupRequestDto.getTripArea()));
    }

    TripGroup tripGroup = TripGroup.builder()
        .name(tripGroupRequestDto.getName())
        .content(tripGroupRequestDto.getContent())
        .maxMemberNumber(tripGroupRequestDto.getMaxMemberNumber())
        .currentMemberNumber(0)
        .tripArea(tripGroupRequestDto.getTripArea())
        .tripDate(tripGroupRequestDto.getTripDate())
        .dueDate(tripGroupRequestDto.getDueDate() == null ? tripGroupRequestDto.getTripDate().minusDays(1)
            : tripGroupRequestDto.getDueDate())
        .thumbnailUrl(tripGroupRequestDto.getThumbnailUrl())
        .status(GroupStatus.OPEN)
        .build();

    return tripGroupRepository.save(tripGroup);
  }

  public TripGroup modifiedTripGroup(TripGroupRequestDto tripGroupRequestDto, MultipartFile file) {

    if (file != null && !file.isEmpty()) {
      String imageUrl = imageService.modifiedImageWithFallback(file, tripGroupRequestDto.getThumbnailUrl(), ImageType.THUMBNAIL);
      tripGroupRequestDto.setThumbnailUrl(imageUrl);
    }

    Optional<TripGroup> foundTripGroup = tripGroupRepository.findById(tripGroupRequestDto.getTripGroupId());

    TripGroup tripGroup = foundTripGroup.orElseThrow(() -> {
      throw new RuntimeException("수정할 그룹 데이터가 존재하지 않습니다.");
    });

    tripGroup.updateTripGroup(tripGroupRequestDto);

    return tripGroup;
  }

  public String getThumbnailUrl(String tripArea) {
    DefaultImage randomByImageName = defaultImageRepository.findRandomByImageName(tripArea);
    return randomByImageName.getDefaultImageUrl();
  }
}
