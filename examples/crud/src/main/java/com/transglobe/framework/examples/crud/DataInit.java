package com.transglobe.framework.examples.crud;

import com.transglobe.framework.examples.crud.rose.customer.repository.CustomerEntity;
import com.transglobe.framework.examples.crud.rose.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataInit {

  final CustomerRepository customerRepository;

  @EventListener(ApplicationReadyEvent.class)
  public void initCustomer() {
    customerRepository.saveAll(List.of(
        customer("Aiden Ramirez"),
        customer("Abraham Weston"),
        customer("Amiee Maynard"),
        customer("Walid Phillips"),
        customer("Jesus Donnelly"),
        customer("Sallie Ahmed"),
        customer("Zarah Rowland"),
        customer("Omari Gregory"),
        customer("Amanah Bullock"),
        customer("Lola Key"),
        customer("Issac Rice"),
        customer("Kieran Morris"),
        customer("Tracey Hendrix"),
        customer("Menaal Ramos"),
        customer("Amy-Leigh Hilton"),
        customer("Graeme Leech"),
        customer("Jose Rogers"),
        customer("Andre Cote"),
        customer("Ira Mccarthy"),
        customer("Izabel Sanders"),
        customer("Amy Davie"),
        customer("Cruz Head"),
        customer("Carmen Sampson"),
        customer("Spike Peel"),
        customer("Clinton Harrington"),
        customer("Sahil Jaramillo"),
        customer("Nial Plummer"),
        customer("Tyla Frost"),
        customer("Aoife Mccallum"),
        customer("Kean Mason"),
        customer("Lyndsey Beech"),
        customer("Evalyn Ford"),
        customer("Ayah Stevenson"),
        customer("Dalia Wilkins"),
        customer("Matthew Villalobos"),
        customer("Jerry Owens"),
        customer("Kaja Livingston"),
        customer("Kenan Alvarado"),
        customer("Sonya Legge"),
        customer("Rees Armitage"),
        customer("Ajay Compton"),
        customer("Olaf Adamson"),
        customer("Nafeesa Roberson"),
        customer("Rania Dickerson"),
        customer("Jeffery Baird"),
        customer("Clayton Aguirre"),
        customer("Jaydon Summers"),
        customer("Chance Medina"),
        customer("Owen Kouma"),
        customer("Sania Marriott")));

  }

  Random random = new Random();
  int birthday_year_max = 2000;
  int birthday_year_min = 1970;

  private CustomerEntity customer(String name) {
    var year = random.nextInt(birthday_year_max - birthday_year_min) + birthday_year_min;
    var month = random.nextInt(12 - 1) + 1;
    var days = YearMonth.of(year, month).lengthOfMonth();
    var dayOfMonth = random.nextInt(days - 1) + 1;
    return CustomerEntity.builder()
        .name(name)
        .birthday(LocalDate.of(year, month, dayOfMonth))
        .password(UUID.randomUUID().toString() + "_lower")
        .build();
  }

}
