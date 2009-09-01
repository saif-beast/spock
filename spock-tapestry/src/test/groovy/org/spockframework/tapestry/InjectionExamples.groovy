/*
 * Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
 
package org.spockframework.tapestry

import org.apache.tapestry5.ioc.ObjectLocator
import spock.lang.Specification
import spock.tapestry.TapestrySupport
import org.apache.tapestry5.ioc.annotations.*

@TapestrySupport
@SubModule(Module1)
class ServiceInjection extends Specification {
  @Inject
  IService1 service1

  @Inject
  IService2 service2

  def "use injected services"() {
    expect:
    service1.generateString() == service2.generateQuickBrownFox()
  }
}

@TapestrySupport
@SubModule(Module1)
class ObjectLocatorInjection extends Specification {
  @Inject
  private ObjectLocator locator

  def "use object locator"() {
    expect:
    locator.getService(IService1).generateString() == locator.getService(IService2).generateQuickBrownFox()
  }
}

@TapestrySupport
@SubModule(Module1)
class SymbolInjection extends Specification {
  @Inject
  @Symbol("java.version")
  String javaVersion

  @Inject
  @Symbol("configKey") // Groovy doesn't currently support the usage of constants in annotations
  String configValue

  @Inject
  @Value("\${java.version} and \${configKey}")
  String computedValue

  def "inject system property"() {
    expect:
    javaVersion == System.getProperty("java.version")
  }

  def "inject application default"() {
    expect:
    configValue == "configValue"
  }

  def "inject computed value"() {
    expect:
    computedValue == "${System.getProperty("java.version")} and configValue"
  }
}