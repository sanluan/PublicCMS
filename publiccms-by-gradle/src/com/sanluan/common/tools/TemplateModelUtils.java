package com.sanluan.common.tools;

import static org.apache.commons.lang3.StringUtils.split;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sanluan.common.base.Base;

import freemarker.ext.beans.BeanModel;
import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateDateModel;
import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;
import freemarker.template.TemplateScalarModel;
import freemarker.template.TemplateSequenceModel;

/**
 * 
 * TemplateModelUtils 模板数据模型工具类
 *
 */
public class TemplateModelUtils extends Base {
    public static final String FULL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final int FULL_DATE_LENGTH = FULL_DATE_FORMAT.length();

    public static final String SHORT_DATE_FORMAT = "yyyy-MM-dd";
    public static final int SHORT_DATE_LENGTH = SHORT_DATE_FORMAT.length();

    /**
     * @param model
     * @return
     * @throws TemplateModelException
     */
    public static Object converBean(TemplateModel model) throws TemplateModelException {
        if (notEmpty(model)) {
            if (model instanceof TemplateSequenceModel) {
                converBean(((TemplateSequenceModel) model).get(0));
            }
            if (model instanceof BeanModel) {
                return ((BeanModel) model).getWrappedObject();
            }
        }
        return null;
    }

    /**
     * @param model
     * @return
     * @throws TemplateModelException
     */
    public static String converString(TemplateModel model) throws TemplateModelException {
        if (notEmpty(model)) {
            if (model instanceof TemplateSequenceModel) {
                converString(((TemplateSequenceModel) model).get(0));
            }
            if (model instanceof TemplateScalarModel) {
                return ((TemplateScalarModel) model).getAsString();
            } else if ((model instanceof TemplateNumberModel)) {
                return ((TemplateNumberModel) model).getAsNumber().toString();
            }
        }
        return null;
    }

    /**
     * @param model
     * @return
     * @throws TemplateModelException
     */
    public static TemplateHashModelEx converMap(TemplateModel model) throws TemplateModelException {
        if (notEmpty(model)) {
            if (model instanceof TemplateHashModelEx) {
                return (TemplateHashModelEx) model;
            }
        }
        return null;
    }

    /**
     * @param model
     * @return
     * @throws TemplateModelException
     */
    public static Integer converInteger(TemplateModel model) throws TemplateModelException {
        if (notEmpty(model)) {
            if (model instanceof TemplateSequenceModel) {
                converInteger(((TemplateSequenceModel) model).get(0));
            }
            if (model instanceof TemplateNumberModel) {
                return ((TemplateNumberModel) model).getAsNumber().intValue();
            } else if (model instanceof TemplateScalarModel) {
                String s = ((TemplateScalarModel) model).getAsString();
                if (notEmpty(s)) {
                    try {
                        return Integer.parseInt(s);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    /**
     * @param model
     * @return
     * @throws TemplateModelException
     */
    public static Short converShort(TemplateModel model) throws TemplateModelException {
        if (notEmpty(model)) {
            if (model instanceof TemplateSequenceModel) {
                model = ((TemplateSequenceModel) model).get(0);
            }
            if (model instanceof TemplateNumberModel) {
                return ((TemplateNumberModel) model).getAsNumber().shortValue();
            } else if (model instanceof TemplateScalarModel) {
                String s = ((TemplateScalarModel) model).getAsString();
                if (notEmpty(s)) {
                    try {
                        return Short.parseShort(s);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    /**
     * @param model
     * @return
     * @throws TemplateModelException
     */
    public static Long converLong(TemplateModel model) throws TemplateModelException {
        if (notEmpty(model)) {
            if (model instanceof TemplateSequenceModel) {
                model = ((TemplateSequenceModel) model).get(0);
            }
            if (model instanceof TemplateNumberModel) {
                return ((TemplateNumberModel) model).getAsNumber().longValue();
            } else if (model instanceof TemplateScalarModel) {
                String s = ((TemplateScalarModel) model).getAsString();
                if (notEmpty(s)) {
                    try {
                        return Long.parseLong(s);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    /**
     * @param model
     * @return
     * @throws TemplateModelException
     */
    public static Double converDouble(TemplateModel model) throws TemplateModelException {
        if (notEmpty(model)) {
            if (model instanceof TemplateSequenceModel) {
                converDouble(((TemplateSequenceModel) model).get(0));
            }
            if (model instanceof TemplateNumberModel) {
                return ((TemplateNumberModel) model).getAsNumber().doubleValue();
            } else if (model instanceof TemplateScalarModel) {
                String s = ((TemplateScalarModel) model).getAsString();
                if (notEmpty(s)) {
                    try {
                        return Double.parseDouble(s);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    /**
     * @param model
     * @return
     * @throws TemplateModelException
     */
    public static String[] converStringArray(TemplateModel model) throws TemplateModelException {
        if (model instanceof TemplateSequenceModel) {
            TemplateSequenceModel smodel = (TemplateSequenceModel) model;
            String[] values = new String[smodel.size()];
            for (int i = 0; i < smodel.size(); i++) {
                values[i] = converString(smodel.get(i));
            }
            return values;
        }
        String str = converString(model);
        if (notEmpty(str)) {
            if (0 <= str.indexOf(COMMA_DELIMITED)) {
                return split(str, ',');
            } else {
                return split(str, ' ');
            }
        }
        return null;
    }

    /**
     * @param model
     * @return
     * @throws TemplateModelException
     */
    public static Boolean converBoolean(TemplateModel model) throws TemplateModelException {
        if (notEmpty(model)) {
            if (model instanceof TemplateSequenceModel) {
                model = ((TemplateSequenceModel) model).get(0);
            }
            if (model instanceof TemplateBooleanModel) {
                return ((TemplateBooleanModel) model).getAsBoolean();
            } else if (model instanceof TemplateNumberModel) {
                return !(0 == ((TemplateNumberModel) model).getAsNumber().intValue());
            } else if (model instanceof TemplateScalarModel) {
                String temp = ((TemplateScalarModel) model).getAsString();
                if (notEmpty(temp)) {
                    return Boolean.valueOf(temp);
                }
            }
        }
        return null;
    }

    /**
     * @param model
     * @return
     * @throws TemplateModelException
     * @throws ParseException
     */
    public static Date converDate(TemplateModel model) throws TemplateModelException, ParseException {
        if (notEmpty(model)) {
            if (model instanceof TemplateSequenceModel) {
                converDate(((TemplateSequenceModel) model).get(0));
            }
            if (model instanceof TemplateDateModel) {
                return ((TemplateDateModel) model).getAsDate();
            } else if (model instanceof TemplateScalarModel) {
                String temp = trimToEmpty(((TemplateScalarModel) model).getAsString());
                if (FULL_DATE_LENGTH == temp.length()) {
                    return new SimpleDateFormat(FULL_DATE_FORMAT).parse(temp);
                } else if (SHORT_DATE_LENGTH == temp.length()) {
                    return new SimpleDateFormat(SHORT_DATE_FORMAT).parse(temp);
                }
            }
        }
        return null;
    }
}
