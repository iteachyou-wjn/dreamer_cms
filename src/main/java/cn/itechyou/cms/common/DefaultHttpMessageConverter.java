/**
 * Original Author -> 杨海健 (taketoday@foxmail.com) https://taketoday.cn
 * Copyright © TODAY & 2017 - 2019 All Rights Reserved.
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *   
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see [http://www.gnu.org/licenses/]
 */
package cn.itechyou.cms.common;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import org.springframework.core.ResolvableType;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author TODAY <br>
 *         2019-10-26 21:56
 */
public class DefaultHttpMessageConverter
        extends AbstractHttpMessageConverter<Object> implements GenericHttpMessageConverter<Object> {

    /**
     * Can serialize/deserialize all types.
     */
    public DefaultHttpMessageConverter() {
        super(MediaType.APPLICATION_JSON_UTF8);
    }

    private final SerializerFeature[] serializeFeatures = { //
        SerializerFeature.WriteMapNullValue, //
        SerializerFeature.WriteNullListAsEmpty, //
        SerializerFeature.DisableCircularReferenceDetect//
    };

    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        return super.canRead(contextClass, mediaType);
    }

    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
        return super.canWrite(clazz, mediaType);
    }

    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) //
            throws IOException, HttpMessageNotReadableException //
    {
        return readType(getType(type, contextClass), inputMessage);
    }

    public void write(Object o, Type type, MediaType contentType, HttpOutputMessage outputMessage) //
            throws IOException, HttpMessageNotWritableException //
    {
        super.write(o, contentType, outputMessage);
    }

    @Override
    protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) //
            throws IOException, HttpMessageNotReadableException //
    {
        return readType(getType(clazz, null), inputMessage);
    }

    /**
     * 
     * @param type
     * @param inputMessage
     * @return
     * @throws IOException
     */
    private Object readType(Type type, HttpInputMessage inputMessage) throws IOException {
        return JSON.parseObject(inputMessage.getBody(),
                                Constant.DEFAULT_CHARSET,
                                type,
                                Feature.DisableCircularReferenceDetect);
    }

    @Override
    protected void writeInternal(final Object value, final HttpOutputMessage outputMessage) //
            throws IOException, HttpMessageNotWritableException //
    {
        //      log.info("value: {}", value);

        JSON.writeJSONString(outputMessage.getBody(),
                             Constant.DEFAULT_CHARSET,
                             value,
                             SerializeConfig.globalInstance,
                             null,
                             Constant.DEFAULT_DATE_FORMAT,
                             JSON.DEFAULT_GENERATE_FEATURE,
                             serializeFeatures);

        //      JSON.writeJSONString(outputMessage.getBody(), value, serializeFeatures);
    }

    private static Type getType(Type type, Class<?> contextClass) {

        if (contextClass != null) {

            ResolvableType resolvedType = ResolvableType.forType(type);
            if (type instanceof TypeVariable) {

                ResolvableType resolvedTypeVariable = //
                        resolveVariable((TypeVariable<?>) type, ResolvableType.forClass(contextClass));

                if (resolvedTypeVariable != ResolvableType.NONE) {
                    return resolvedTypeVariable.resolve();
                }
            }
            else if (type instanceof ParameterizedType && resolvedType.hasUnresolvableGenerics()) {

                final ParameterizedType parameterizedType = (ParameterizedType) type;
                final Type[] typeArguments = parameterizedType.getActualTypeArguments();
                final Class<?>[] generics = new Class[typeArguments.length];

                for (int i = 0; i < typeArguments.length; ++i) {

                    Type typeArgument = typeArguments[i];
                    if (typeArgument instanceof TypeVariable) {
                        ResolvableType resolvedTypeArgument = //
                                resolveVariable((TypeVariable<?>) typeArgument, ResolvableType.forClass(contextClass));
                        if (resolvedTypeArgument != ResolvableType.NONE) {
                            generics[i] = resolvedTypeArgument.resolve();
                        }
                        else {
                            generics[i] = ResolvableType.forType(typeArgument).resolve();
                        }
                    }
                    else {
                        generics[i] = ResolvableType.forType(typeArgument).resolve();
                    }
                }

                return ResolvableType.forClassWithGenerics(resolvedType.getRawClass(), generics).getType();
            }
        }

        return type;
    }

    private static ResolvableType resolveVariable(TypeVariable<?> typeVariable, ResolvableType contextType) {

        ResolvableType resolvedType;

        if (contextType.hasGenerics()) {
            resolvedType = ResolvableType.forType(typeVariable, contextType);
            if (resolvedType.resolve() != null) {
                return resolvedType;
            }
        }

        ResolvableType superType = contextType.getSuperType();
        if (superType != ResolvableType.NONE) {
            resolvedType = resolveVariable(typeVariable, superType);
            if (resolvedType.resolve() != null) {
                return resolvedType;
            }
        }
        for (ResolvableType ifc : contextType.getInterfaces()) {
            resolvedType = resolveVariable(typeVariable, ifc);
            if (resolvedType.resolve() != null) {
                return resolvedType;
            }
        }
        return ResolvableType.NONE;
    }

}
