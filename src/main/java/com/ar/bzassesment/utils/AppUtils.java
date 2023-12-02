package com.ar.bzassesment.utils;

import com.ar.bzassesment.dao.entity.Instructions;

import java.util.List;
import java.util.stream.Collectors;

public class AppUtils {

    private AppUtils(){

    }

    public static String getInstructionsAsString(List<Instructions> instructionsList) {
        return instructionsList.stream()
                .map(instructions -> instructions.getInsSteps())
                .collect(Collectors.joining(", "));
    }
}
