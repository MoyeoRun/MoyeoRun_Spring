package com.moyeorun.auth.domain.auth.domain;

import com.moyeorun.auth.domain.auth.domain.contant.RoleType;
import com.moyeorun.auth.domain.model.BaseTimeEntity;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "user", uniqueConstraints = {
    @UniqueConstraint(name = "snsIdentify", columnNames = {"sns_id", "sns_provider_type"})
})
public class User extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "height", nullable = false)
  private Double height;

  @Column(name = "weight", nullable = false)
  private Double weight;

  @Column(name = "image", nullable = false)
  private String image;

  @Column(name = "nick_name", nullable = false)
  private String nickName;

  @Column(name = "fcm_token", nullable = true)
  private String fcmToken;

  @Column(nullable = false)
  @Embedded
  private SnsIdentify snsIdentify;

  @Column(name = "role", nullable = false)
  @Enumerated(EnumType.STRING)
  private RoleType role;

  @Builder
  User(String name, String email, Double height, Double weight, String image, String nickName,
      SnsIdentify snsIdentify) {
    this.name = name;
    this.email = email;
    this.height = height;
    this.weight = weight;
    this.image = image;
    this.nickName = nickName;
    this.snsIdentify = snsIdentify;
    this.role = RoleType.USER;
  }
}
