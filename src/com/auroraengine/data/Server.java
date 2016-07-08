/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.data;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A tag that is attached to anything that is only important to the server side
 * of the program and so shouldn't need to be included in the client side
 * program. Need to determine how to use this properly to reduce the size of
 * the client and server objects whilst developing them in the same environment.
 * @author Arthur
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.LOCAL_VARIABLE, ElementType.FIELD})
public @interface Server {}