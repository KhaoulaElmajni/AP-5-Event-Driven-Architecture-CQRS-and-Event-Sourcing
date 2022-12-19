# <strong style="color:blue; opacity: 0.80">AP-5-Event-Driven-Architecture-CQRS-and-Event-Sourcing</strong> :mortar_board:
## <span style="color:green "> 1.Project Presentation</span>
 * <strong style="color:dark"> Créer une application qui permet de gérer des comptes bancaires.
	* <strong style="color:dark"> Permet de :  :100: 
1. #### Ajouter un Compte
2. #### Activer un compte après création
3. #### Créditer un compte
4. #### Débiter un compte
5. #### Consulter un compte
6. #### Consulter les comptes
7. #### Consulter les opérations d’un compte
8. #### Suivre en temps réel l’état d’un compte

# <span style="color:green "> 2.Architecture de l'activité pratique</span>
	
![](https://i.imgur.com/DCbtJ98.png)

___
![](https://i.imgur.com/2PXpj3O.png)![](https://i.imgur.com/6qSBzCo.png)



# <span style="color:green">2.Technologies utilisées </span>

	
 #### <span style="color:#0036ad"> 1.Axon</span>
 * <strong style="color:dark">* <strong style="color:dark">AXON est un framework Open Source pour les microservices pilotés par les événements et la conception pilotée par le domaine. Il s'agit d'un framework Java qui permet aux développeurs d'accéder aux référentiels, aux implémentations de composants cruciaux, notamment les agrégats, les bus de commandes et d'événements, et aux implémentations du modèle architectural CQRS.

*voir également à propos* [AXON](https://www.axoniq.io/):link: 
	
	
 #### <span style="color:#0036ad"> 2.Spring Boot</span>
 * <strong style="color:dark">* <strong style="color:dark">Spring Boot facilite la création d'applications Spring autonomes de qualité production que vous pouvez "just run".

*voir également à propos* [Spring Boot](https://spring.io/projects/spring-boot):link: 

# <span style="color:green">3.Design Patterns </span>

#### <span style="color:#0036ad"> **1.Event Sourcing**</span>
 * <strong style="color:dark">Pattern d’architecture <strong style="color:dark">
• Capturer tous les changements de l’état d’une application Comme Séquence d’Evénements
• Ne pas se concentrer sur l’état courant de l’application, mais sur la séquence de changements d’états (Evénements métiers) qui ont abouti à l’état courant
• À partir de cette séquences d’événements, on pourra agréger l’état courant de l’application
• Tout changement de l’état de l’application a une cause unique (Evénement)

*voir également à propos* [Event Sourcing](https://learn.microsoft.com/en-us/azure/architecture/patterns/event-sourcing):link: 
	
	
#### <span style="color:#0036ad"> **1.CQRS : Command Query Responsability Segregation**
</span>
 * <strong style="color:dark">Pattern d’architecture <strong style="color:dark">

Pattern qui consiste à séparer la partie lecture de la partie écriture de l’application.

*voir également à propos* [CQRS](https://learn.microsoft.com/en-us/azure/architecture/patterns/cqrs):link: 	
	
___
* <strong style="color:green"> Architecture et terminologie 
	
![](https://i.imgur.com/Bh7ydqO.png)


# <span style="color:green">4.Réalisation</span>

## **Partie Commands**
	
> 	Classe générique de command
	
```java!
public abstract class BaseCommand <T> {
    @TargetAggregateIdentifier
    @Getter private T id;

    public BaseCommand(T id) {
        this.id = id;
    }
}
```
> Classe de commande de création de compte

```java!

public class CreateAccountCommand extends BaseCommand<String>{

    @Getter private double initialeBalance;
    @Getter private  String currency;
    public CreateAccountCommand(String id, double initialeBalance, String currency) {
        super(id);
        this.initialeBalance = initialeBalance;
        this.currency = currency;
    }
}

```
	
> Classe de Commande de crédit

```java!
public class CreditAccountCommand extends BaseCommand<String>{

    @Getter private double amount;
    @Getter private  String currency;
    public CreditAccountCommand(String id, double amount, String currency) {
        super(id);
        this.amount = amount;
        this.currency = currency;
    }
}
```

> Classe de commande de débit
	
```java!
public class DebitAccountCommand extends BaseCommand<String>{

    @Getter private double amount;
    @Getter private  String currency;
    public DebitAccountCommand(String id, double amount, String currency) {
        super(id);
        this.amount = amount;
        this.currency = currency;
    }
}
```

## **Partie Query**
> Classe Account
```java!
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
    @Id
    private String id;
    private double balance;
    private Instant createdAt;
    private String currency;
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
    @OneToMany(mappedBy = "account")
    private Collection<AccountTransaction> transactions;
}
```
	
> Classe Transaction
```java!
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccountTransaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date timestamp;
    private double amount;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Account account;

}
```
	
# <span style="color:green">5.Simulation</span>
