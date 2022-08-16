package com.moyeorun.api.domain.user.domain;

import com.moyeorun.api.domain.user.domain.contant.SnsProviderType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class SnsIdentify {

  @Column(name = "sns_id")
  private String snsId;

  @Column(name = "sns_provider_type")
  @Enumerated(EnumType.STRING)
  private SnsProviderType snsProviderType;

  public SnsIdentify(String snsId, SnsProviderType snsProviderType) {
    this.snsId = snsId;
    this.snsProviderType = snsProviderType;
  }
}
