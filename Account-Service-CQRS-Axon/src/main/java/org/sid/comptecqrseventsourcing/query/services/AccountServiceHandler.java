package org.sid.comptecqrseventsourcing.query.services;


import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.sid.comptecqrseventsourcing.commonapi.enums.OperationType;
import org.sid.comptecqrseventsourcing.commonapi.events.AccountActivatedEvent;
import org.sid.comptecqrseventsourcing.commonapi.events.AccountCreatedEvent;
import org.sid.comptecqrseventsourcing.commonapi.events.AccountCreditedEvent;
import org.sid.comptecqrseventsourcing.commonapi.events.AccountDebitedEvent;
import org.sid.comptecqrseventsourcing.commonapi.queries.GetAccountByIdQuery;
import org.sid.comptecqrseventsourcing.commonapi.queries.GetAllAccountsQuery;
import org.sid.comptecqrseventsourcing.query.entities.Account;
import org.sid.comptecqrseventsourcing.query.entities.Operation;
import org.sid.comptecqrseventsourcing.query.repositories.AccountRepository;
import org.sid.comptecqrseventsourcing.query.repositories.OperationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Slf4j   // for logging
@Transactional
public class AccountServiceHandler {
    private AccountRepository accountRepository;
    private OperationRepository operationRepository;

    public AccountServiceHandler(AccountRepository accountRepository, OperationRepository operationRepository) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
    }

    @EventHandler
    public void on(AccountCreatedEvent event)
    {
        log.info("AccountCreatedEvent recieved!");
        Account account = new Account();
        account.setId(event.getId());
        account.setAccountStatus(event.getAccountStatus());
        account.setBalance(event.getInitialeBalance());
        account.setCurrency(event.getCurrency());

         accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountActivatedEvent event)
    {
        log.info("AccountActivatedEvent recieved!");
        Account account = accountRepository.findById(event.getId()).get();
        account.setAccountStatus(event.getAccountStatus());
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountCreditedEvent event)
    {
        log.info("AccountCreditedEvent recieved!");
        Account account = accountRepository.findById(event.getId()).get();
        Operation operation = new Operation();
        operation.setOperationType(OperationType.CREDIT);
        operation.setAmount(event.getAmount());
        operation.setDate(new Date());  // not this date -- should put the date of the operation
        operation.setAccount(account);
        operationRepository.save(operation);
        account.setBalance(account.getBalance() + event.getAmount());
        accountRepository.save(account);
    }


    @EventHandler
    public void on(AccountDebitedEvent event)
    {
        log.info("AccountDebitedEvent recieved!");
        Account account = accountRepository.findById(event.getId()).get();
        Operation operation = new Operation();
        operation.setOperationType(OperationType.DEBIT);
        operation.setAmount(event.getAmount());
        operation.setDate(new Date());  // not this date -- should put the date of the operation
        operation.setAccount(account);
        operationRepository.save(operation);
        account.setBalance(account.getBalance() - event.getAmount());
        accountRepository.save(account);
    }

    @QueryHandler
    public List<Account> on(GetAllAccountsQuery query)
    {
        return accountRepository.findAll();
    }

    @QueryHandler
    public Account on(GetAccountByIdQuery query)
    {
        return accountRepository.findById(query.getId()).get();
    }

}
