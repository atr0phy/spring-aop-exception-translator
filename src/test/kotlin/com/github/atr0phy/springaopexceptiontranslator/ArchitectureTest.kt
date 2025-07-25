package com.github.atr0phy.springaopexceptiontranslator

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import org.junit.jupiter.api.Test

class ArchitectureTest {

    private val importedClasses: JavaClasses = ClassFileImporter()
        .importPackages("com.github.atr0phy.springaopexceptiontranslator")

    @Test
    fun `ドメイン層は他の層に依存してはいけない`() {
        noClasses()
            .that()
            .resideInAPackage("..domain..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..application..", "..infrastructure..", "..presentation..")
            .check(importedClasses)
    }

    @Test
    fun `ドメイン層はSpringフレームワークに依存してはいけない`() {
        noClasses()
            .that()
            .resideInAPackage("..domain..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("org.springframework..")
            .check(importedClasses)
    }

    @Test
    fun `アプリケーション層はインフラ層の具象クラスに依存してはいけない`() {
        noClasses()
            .that()
            .resideInAPackage("..application..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..infrastructure..")
            .check(importedClasses)
    }

    @Test
    fun `アプリケーション層はプレゼンテーション層に依存してはいけない`() {
        noClasses()
            .that()
            .resideInAPackage("..application..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..presentation..")
            .check(importedClasses)
    }

    @Test
    fun `プレゼンテーション層はインフラ層に依存してはいけない`() {
        noClasses()
            .that()
            .resideInAPackage("..presentation..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..infrastructure..")
            .check(importedClasses)
    }

    @Test
    fun `UseCase クラスは Service アノテーションを持つべき`() {
        classes()
            .that()
            .haveNameMatching(".*UseCase")
            .should()
            .beAnnotatedWith("org.springframework.stereotype.Service")
            .check(importedClasses)
    }

    @Test
    fun `Repository インターフェースはドメイン層に配置されるべき`() {
        classes()
            .that()
            .haveNameMatching(".*Repository")
            .and()
            .areInterfaces()
            .should()
            .resideInAPackage("..domain.repository..")
            .check(importedClasses)
    }

    @Test
    fun `Repository 実装クラスはインフラ層に配置されるべき`() {
        classes()
            .that()
            .haveNameMatching(".*RepositoryImpl")
            .should()
            .resideInAPackage("..infrastructure..")
            .check(importedClasses)
    }

    @Test
    fun `Controller クラスはプレゼンテーション層に配置されるべき`() {
        classes()
            .that()
            .haveNameMatching(".*Controller")
            .should()
            .resideInAPackage("..presentation..")
            .check(importedClasses)
    }

    @Test
    fun `Exception クラスは適切な層に配置されるべき`() {
        classes()
            .that()
            .haveNameMatching(".*Exception")
            .and()
            .areNotNestedClasses()
            .should()
            .resideInAnyPackage("..domain.exception..", "..infrastructure.exception..")
            .check(importedClasses)
    }
}