package org.sid.comptecqrseventsourcing.query.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.sid.comptecqrseventsourcing.commonapi.queries.GetAccountByIdQuery;
import org.sid.comptecqrseventsourcing.commonapi.queries.GetAllAccountsQuery;
import org.sid.comptecqrseventsourcing.query.entities.Account;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/query/accounts")
@Slf4j
@AllArgsConstructor
public class AccountQueryController {

    //=> QueryGateway is used to send queries to the query bus
    private QueryGateway queryGateway;


    @GetMapping("/allAccounts")
    public CompletableFuture<List<Account>> accountList() {
        CompletableFuture<List<Account>> query = queryGateway.query(new GetAllAccountsQuery(), ResponseTypes.multipleInstancesOf(Account.class));
        return query;
    }
    @GetMapping("/{id}")
    public Account getAccount(@PathVariable String id) {
        Account response = queryGateway.query(new GetAccountByIdQuery(id), ResponseTypes.instanceOf(Account.class)).join();
        return response;
    }

    /*@GetMapping(path = "/accountOperations/{accountId}")
    public List<AccountOperationResponseDTO> accountOperationList(@PathVariable String accountId){
        return queryGateway.query(new
                GetAccountOperationsQueryDTO(accountId),ResponseTypes.multipleInstancesOf(AccountOperationResponseDT
                O.class)).join();
    }


    @GetMapping(value = "/{accountId}/watch"
            ,produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AccountOperationResponseDTO> watch(@PathVariable String accountId){
        @GetMapping(value = "/{accountId}/watch"
                ,produces = MediaType.TEXT_EVENT_STREAM_VALUE)
        public Flux<BankAccountResponseDTO> watch(@PathVariable String accountId){
            SubscriptionQueryResult<BankAccountResponseDTO,BankAccountResponseDTO> result=
                    queryGateway.subscriptionQuery(
                            new GetAccountQueryDTO(accountId),
                            ResponseTypes.instanceOf(BankAccountResponseDTO.class),
                            ResponseTypes.instanceOf(BankAccountResponseDTO.class)
                    );
            return result.initialResult().concatWith(result.updates());
        // return result.initialResult().flatMapMany(Flux::fromIterable).concatWith(result.updates());
        }
    }
*/
}
