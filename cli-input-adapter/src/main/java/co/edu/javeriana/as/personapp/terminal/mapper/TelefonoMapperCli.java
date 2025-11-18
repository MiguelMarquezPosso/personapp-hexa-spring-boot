package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.terminal.model.TelefonoModelCli;

@Mapper
public class TelefonoMapperCli {

  public TelefonoModelCli fromDomainToAdapterCli(Phone phone) {
    TelefonoModelCli telefonoModelCli = new TelefonoModelCli();
    telefonoModelCli.setNumero(phone.getNumber());
    telefonoModelCli.setOperadora(phone.getCompany());
    telefonoModelCli.setDuenioCc(phone.getOwner() != null ? phone.getOwner().getIdentification() : null);
    return telefonoModelCli;
  }
}
