/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author LittleRover
 */
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.LOCAL_VARIABLE,
				 ElementType.FIELD})
@Retention(RetentionPolicy.CLASS)
public @interface NotNull {

}
