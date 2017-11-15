package br.com.payup.userservice.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Card entity.
 */
public class CardDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 19, max = 19)
    private String number;

    @NotNull
    @Size(min = 5, max = 5)
    private String expirationDate;

    @NotNull
    private String name;

    @NotNull
    @Size(min = 3, max = 3)
    private String cvv;

    private Long consumerId;

    private String consumerName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public Long getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(Long consumerId) {
        this.consumerId = consumerId;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CardDTO cardDTO = (CardDTO) o;
        if(cardDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cardDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CardDTO{" +
            "id=" + getId() +
            ", number='" + getNumber() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", name='" + getName() + "'" +
            ", cvv='" + getCvv() + "'" +
            "}";
    }
}
