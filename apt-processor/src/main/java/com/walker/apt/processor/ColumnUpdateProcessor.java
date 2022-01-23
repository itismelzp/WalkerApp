package com.walker.apt.processor;

import com.google.auto.service.AutoService;
import com.walker.apt.annotation.ColumnUpdate;

import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

/**
 * entity属性升级注解
 * <p>
 * Created by walkerzpli on 2022/1/23.
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ColumnUpdateProcessor extends BaseProcessor {
    @Override
    protected Class<?>[] getSupportedAnnotation() {
        return new Class[] {ColumnUpdate.class};
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return false;
    }
}
