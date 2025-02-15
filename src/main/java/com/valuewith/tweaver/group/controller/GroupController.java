package com.valuewith.tweaver.group.controller;

import com.valuewith.tweaver.chat.entity.ChatRoom;
import com.valuewith.tweaver.chat.service.ChatRoomService;
import com.valuewith.tweaver.constants.ImageType;
import com.valuewith.tweaver.defaultImage.service.ImageService;
import com.valuewith.tweaver.group.dto.TripGroupRequestDto;
import com.valuewith.tweaver.group.entity.TripGroup;
import com.valuewith.tweaver.group.service.TripGroupService;
import com.valuewith.tweaver.groupMember.service.GroupMemberService;
import com.valuewith.tweaver.menber.entity.Member;
import com.valuewith.tweaver.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/groups/*")
public class GroupController {
  private final ImageService imageService;
  private final TripGroupService tripGroupService;
  private final PlaceService placeService;
  private final ChatRoomService chatRoomService;
  private final GroupMemberService groupMemberService;

  @PostMapping
  public ResponseEntity<String> createGroup(
      @RequestPart(value = "tripGroupRequestDto") TripGroupRequestDto tripGroupRequestDto,
      @RequestPart(value = "file") MultipartFile file) {
    // 1.그룹 등록
    TripGroup tripGroup = tripGroupService.createTripGroup(tripGroupRequestDto, file);
    // 2.여행 등록
    placeService.createPlace(tripGroup, tripGroupRequestDto.getPlaces());
    // 3.채팅 등록
    ChatRoom chatRoom = chatRoomService.createChatRoom(tripGroup);
    // 4.멤버 등록(인증된 user값으로 등록) -> 일단 수기로 작성
    // TODO: spring security 인증작업이 끝나면 해당 기능 사용해서 현재 사용자정보 가져오기
    Member member = Member.builder()
        .memberId(1L)
        .email("dodunge@gmail.com")
        .password("1234")
        .nickName("수정")
        .age(20)
        .gender("여성")
        .profileUrl("http://images...")
        .isSocial(true)
        .build();
    groupMemberService.createGroupMember(tripGroup, member, chatRoom);
    return ResponseEntity.ok("ok");
  }

  @PutMapping
  public ResponseEntity<String> modifiedGroup(
      @RequestPart(value = "tripGroupRequestDto") TripGroupRequestDto tripGroupRequestDto,
      @RequestPart(value = "file") MultipartFile file) {
    // 1.그룹 수정
    TripGroup tripGroup = tripGroupService.modifiedTripGroup(tripGroupRequestDto, file);
    // 2.여행 수정
    placeService.modifiedPlace(tripGroup, tripGroupRequestDto.getPlaces());
    // 3.채팅 수정(그룹명 수정으로 인한 채팅룸 제목 수정)
    chatRoomService.modifiedChatRoom(tripGroup);
    return ResponseEntity.ok("ok");
  }

}
