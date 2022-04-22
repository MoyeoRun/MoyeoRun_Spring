package com.moyeorun.auth.domain.auth.domain;

import com.moyeorun.auth.domain.auth.domain.contant.SnsProviderType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class SnsIdentify {

  @Column(name = "sns_id")
  private String snsId;

  @Column(name = "sns_provider_type")
  private SnsProviderType snsProviderType;

  public SnsIdentify(String snsId, SnsProviderType snsProviderType) {
    this.snsId = snsId;
    this.snsProviderType = snsProviderType;
  }
}
