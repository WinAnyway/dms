package pl.com.bottega.dms.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import pl.com.bottega.dms.application.DocumentCatalog;
import pl.com.bottega.dms.application.DocumentFlowProcess;
import pl.com.bottega.dms.application.ReadingConfirmator;
import pl.com.bottega.dms.application.impl.StandardDocumentFlowProcess;
import pl.com.bottega.dms.application.impl.StandardReadingConfirmator;
import pl.com.bottega.dms.application.user.AuthProcess;
import pl.com.bottega.dms.application.user.CurrentUser;
import pl.com.bottega.dms.application.user.UserRepository;
import pl.com.bottega.dms.application.user.impl.StandardAuthProcess;
import pl.com.bottega.dms.application.user.impl.StandardCurrentUser;
import pl.com.bottega.dms.model.document.DocumentFactory;
import pl.com.bottega.dms.model.document.DocumentRepository;
import pl.com.bottega.dms.model.document.DocumentStatus;
import pl.com.bottega.dms.model.numbers.*;
import pl.com.bottega.dms.model.printing.*;
import pl.com.bottega.dms.model.validation.*;

import java.util.concurrent.Executor;

@org.springframework.context.annotation.Configuration
@EnableAsync
public class Configuration extends AsyncConfigurerSupport {

    @Bean
    public DocumentFlowProcess documentFlowProcess(DocumentFactory documentFactory,
                                                   PrintCostCalculator printCostCalculator,
                                                   DocumentRepository documentRepository,
                                                   CurrentUser currentUser,
                                                   ApplicationEventPublisher publisher,
                                                   DocumentValidator documentValidator
    ) {
        return new StandardDocumentFlowProcess(documentFactory, printCostCalculator, documentRepository, currentUser, publisher, documentValidator);
    }

    @Bean
    public DocumentFactory documentFactory(NumberGenerator numberGenerator) {
        return new DocumentFactory(numberGenerator);
    }

    @Bean
    public AuthProcess authProcess(UserRepository userRepository, CurrentUser currentUser) {
        return new StandardAuthProcess(userRepository, currentUser);
    }

    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
    public CurrentUser currentUser() {
        return new StandardCurrentUser();
    }

    @Bean
    public UserRepository userRepository() {
        return new JPAUserRepository();
    }

    @Bean
    public NumberGenerator numberGenerator(
            @Value("${dms.qualitySystem}") String qualitySystem,
            Environment env
    ) {
        NumberGenerator base;

        if (qualitySystem.equals("ISO"))
            base = new ISONumberGenerator();
        else if (qualitySystem.equals("QEP"))
            base = new QEPNumberGenerator();
        else
            throw new IllegalArgumentException("Unknown quality system");

        if (hasProfile("demo", env))
            base = new DemoNumberGenerator(base);
        if (hasProfile("audit", env))
            base = new AuditNumberGenerator(base);


        return base;
    }

    private boolean hasProfile(String profile, Environment env) {
        for (String activeProgile : env.getActiveProfiles()) {
            if (activeProgile.equals(profile))
                return true;
        }
        return false;
    }

    @Bean
    public PrintCostCalculator printCostCalculator(@Value("${dms.printingSystem}") String printingSystem) {
        PrintCostCalculator baseCalculator;

        if (printingSystem.equals("BW"))
            baseCalculator = new BWPrintCostCalculator();
        else if (printingSystem.equals("RGB"))
            baseCalculator = new RGBPrintCostCalculator();
        else
            throw new IllegalArgumentException("Unknow printing system");

        return new ManualPrintCostCalculator(new PagesCountPrintCostCalculator(baseCalculator));
    }

    @Bean
    public DocumentValidator documentValidator(@Value("${dms.qualitySystem}") String qualitySystem) {
        if (qualitySystem.equals("ISO")) {
            return isoDocumentValidator();
        } else if (qualitySystem.equals("QEP")) {
            return qepDocumentValidator();
        } else
            throw new IllegalArgumentException("Unknown quality system");
    }

    private DocumentValidator qepDocumentValidator() {
        DocumentValidator v1 = new VerifiedAuthorValidator();
        DocumentValidator v2 = new ExpiresAtValidator(DocumentStatus.VERIFIED);
        DocumentValidator v3 = new PublishedContentValidator();
        DocumentValidator v4 = new AgreeableDocumentValidator();
        v1.setNext(v2);
        v2.setNext(v3);
        v3.setNext(v4);
        return v1;
    }

    private DocumentValidator isoDocumentValidator() {
        DocumentValidator v1 = new VerifiedNumberValidator();
        DocumentValidator v2 = new ExpiresAtValidator(DocumentStatus.PUBLISHED);
        DocumentValidator v3 = new AgreeableDocumentValidator();
        v1.setNext(v2);
        v2.setNext(v3);
        return v1;
    }

    @Bean
    public DocumentCatalog documentCatalog() {
        return new JPADocumentCatalog();
    }

    @Bean
    public DocumentRepository documentRepository() {
        return new JPADocumentRepository();
    }

    @Bean
    public ReadingConfirmator readingConfirmator(DocumentRepository repo) {
        return new StandardReadingConfirmator(repo);
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("DMS-Async-Executor");
        executor.initialize();
        return executor;
    }


}
