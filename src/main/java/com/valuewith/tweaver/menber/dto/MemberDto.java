package com.valuewith.tweaver.menber.dto;

import com.valuewith.tweaver.alert.dto.AlertDto;
import com.valuewith.tweaver.groupMember.dto.GroupMemberDto;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class MemberDto {
  private Long memberId;
  private String email;
  private String password;
  private String nickName;
  private Integer age;
  private String gender;
  private String profileUrl;
  private Boolean isSocial;
  private List<GroupMemberDto> groupMembers = new ArrayList<>();
  private List<AlertDto> alerts = new ArrayList<>();
}
