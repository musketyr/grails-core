/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.grails.plugins.databinding

import grails.util.GrailsUtil
import grails.web.databinding.DataBindingUtils
import grails.web.databinding.GrailsWebDataBinder
import org.grails.web.databinding.bindingsource.DataBindingSourceRegistry
import org.grails.web.databinding.bindingsource.DefaultDataBindingSourceRegistry
import org.grails.web.databinding.bindingsource.HalJsonDataBindingSourceCreator
import org.grails.web.databinding.bindingsource.HalXmlDataBindingSourceCreator
import org.grails.web.databinding.bindingsource.JsonDataBindingSourceCreator
import org.grails.web.databinding.bindingsource.XmlDataBindingSourceCreator
import org.grails.databinding.converters.CurrencyValueConverter
import org.grails.databinding.converters.DateConversionHelper
import org.grails.databinding.converters.TimeZoneConverter
import org.grails.databinding.converters.web.LocaleAwareBigDecimalConverter
import org.grails.databinding.converters.web.LocaleAwareNumberConverter

/**
 * @author Jeff Brown
 * @since 2.3
 */
class DataBindingGrailsPlugin {

    def version = GrailsUtil.getGrailsVersion()

    def doWithSpring = {
        def databindingConfig

        databindingConfig = application?.config?.grails?.databinding

        def autoGrowCollectionLimitSetting = databindingConfig?.autoGrowCollectionLimit

        "${DataBindingUtils.DATA_BINDER_BEAN_NAME}"(GrailsWebDataBinder, ref('grailsApplication')) {

            // trimStrings defaults to TRUE
            trimStrings = !Boolean.FALSE.equals(databindingConfig?.trimStrings)

            // convertEmptyStringsToNull defaults to TRUE
            convertEmptyStringsToNull = !Boolean.FALSE.equals(databindingConfig?.convertEmptyStringsToNull)

            // autoGrowCollectionLimit defaults to 256
            if(autoGrowCollectionLimitSetting instanceof Integer) {
                autoGrowCollectionLimit = autoGrowCollectionLimitSetting
            }
        }

        timeZoneConverter(TimeZoneConverter)

        defaultDateConverter(DateConversionHelper) {
            if(databindingConfig?.dateFormats instanceof List) {
                formatStrings = databindingConfig.dateFormats
            }
        }
        [Short,   Short.TYPE,
         Integer, Integer.TYPE,
         Float,   Float.TYPE,
         Long,    Long.TYPE,
         Double,  Double.TYPE].each { numberType ->
            "defaultGrails${numberType.name}Converter"(LocaleAwareNumberConverter) {
                targetType = numberType
            }
        }
        defaultGrailsBigDecimalConverter(LocaleAwareBigDecimalConverter) {
            targetType = BigDecimal
        }
        defaultGrailsBigIntegerConverter(LocaleAwareBigDecimalConverter) {
            targetType = BigInteger
        }

        "${DataBindingSourceRegistry.BEAN_NAME}"(DefaultDataBindingSourceRegistry)

        xmlDataBindingSourceCreator(XmlDataBindingSourceCreator)
        jsonDataBindingSourceCreator(JsonDataBindingSourceCreator)
        halJsonDataBindingSourceCreator(HalJsonDataBindingSourceCreator)
        halXmlDataBindingSourceCreator(HalXmlDataBindingSourceCreator)

        defaultCurrencyConverter CurrencyValueConverter
    }
}
