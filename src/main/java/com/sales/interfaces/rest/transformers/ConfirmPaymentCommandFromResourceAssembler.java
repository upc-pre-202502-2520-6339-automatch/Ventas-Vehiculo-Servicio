package com.sales.interfaces.rest.transformers;

import com.sales.domain.model.commands.ConfirmPaymentCommand;

public class ConfirmPaymentCommandFromResourceAssembler {
    public static ConfirmPaymentCommand toCommand(Long id){ return new ConfirmPaymentCommand(id); }
}