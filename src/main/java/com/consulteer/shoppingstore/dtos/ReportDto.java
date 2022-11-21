package com.consulteer.shoppingstore.dtos;

import java.util.List;

public record ReportDto(String name,
                        Double price,
                        List<CityDto> cities,
                        Double totalIncome) {

}
