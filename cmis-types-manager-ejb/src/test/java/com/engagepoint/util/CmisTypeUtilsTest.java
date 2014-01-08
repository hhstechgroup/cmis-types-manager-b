package com.engagepoint.util;

import com.engagepoint.exception.AppException;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.AbstractTypeDefinition;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * User: victor.klymenko
 * Date: 1/8/14
 * Time: 1:29 PM
 */
public class CmisTypeUtilsTest {

    @Test
    public void testReadFromXML() throws AppException {
        try {
            InputStream stream = new FileInputStream("D:\\GoodProjects\\cmis-types-manager-b\\cmis-types-manager-ejb\\src\\test\\resources\\files\\rel1.xml");
            List<AbstractTypeDefinition> list = CmisTypeUtils.readFromXML(stream);
            Assert.assertNotNull(list);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
