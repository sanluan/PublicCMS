package com.publiccms.common.tools;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.publiccms.common.constants.Constants;

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
 * 模板数据模型工具类
 * 
 * TemplateModelUtils
 *
 */
public class TemplateModelUtils {

    /**
     * @param model
     * @return java bean value
     * @throws TemplateModelException
     */
    public static Object converBean(TemplateModel model) throws TemplateModelException {
        if (null != model) {
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
     * @return string value
     * @throws TemplateModelException
     */
    public static String converString(TemplateModel model) throws TemplateModelException {
        if (null != model) {
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
     * @return map value
     * @throws TemplateModelException
     */
    public static TemplateHashModelEx converMap(TemplateModel model) throws TemplateModelException {
        if (null != model) {
            if (model instanceof TemplateHashModelEx) {
                return (TemplateHashModelEx) model;
            }
        }
        return null;
    }

    /**
     * @param model
     * @return int value
     * @throws TemplateModelException
     */
    public static Integer converInteger(TemplateModel model) throws TemplateModelException {
        if (null != model) {
            if (model instanceof TemplateSequenceModel) {
                converInteger(((TemplateSequenceModel) model).get(0));
            }
            if (model instanceof TemplateNumberModel) {
                return ((TemplateNumberModel) model).getAsNumber().intValue();
            } else if (model instanceof TemplateScalarModel) {
                String s = ((TemplateScalarModel) model).getAsString();
                if (CommonUtils.notEmpty(s)) {
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
     * @return short value
     * @throws TemplateModelException
     */
    public static Short converShort(TemplateModel model) throws TemplateModelException {
        if (null != model) {
            if (model instanceof TemplateSequenceModel) {
                model = ((TemplateSequenceModel) model).get(0);
            }
            if (model instanceof TemplateNumberModel) {
                return ((TemplateNumberModel) model).getAsNumber().shortValue();
            } else if (model instanceof TemplateScalarModel) {
                String s = ((TemplateScalarModel) model).getAsString();
                if (CommonUtils.notEmpty(s)) {
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
     * @return long value
     * @throws TemplateModelException
     */
    public static Long converLong(TemplateModel model) throws TemplateModelException {
        if (null != model) {
            if (model instanceof TemplateSequenceModel) {
                model = ((TemplateSequenceModel) model).get(0);
            }
            if (model instanceof TemplateNumberModel) {
                return ((TemplateNumberModel) model).getAsNumber().longValue();
            } else if (model instanceof TemplateScalarModel) {
                String s = ((TemplateScalarModel) model).getAsString();
                if (CommonUtils.notEmpty(s)) {
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
     * @return double value
     * @throws TemplateModelException
     */
    public static Double converDouble(TemplateModel model) throws TemplateModelException {
        if (null != model) {
            if (model instanceof TemplateSequenceModel) {
                converDouble(((TemplateSequenceModel) model).get(0));
            }
            if (model instanceof TemplateNumberModel) {
                return ((TemplateNumberModel) model).getAsNumber().doubleValue();
            } else if (model instanceof TemplateScalarModel) {
                String s = ((TemplateScalarModel) model).getAsString();
                if (CommonUtils.notEmpty(s)) {
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
     * @return string array value
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
        if (null != str) {
            if (str.contains(Constants.COMMA_DELIMITED)) {
                return StringUtils.split(str, Constants.COMMA_DELIMITED);
            } else {
                return StringUtils.split(str, Constants.BLANK_SPACE);
            }
        }
        return null;
    }

    /**
     * @param model
     * @return bool value
     * @throws TemplateModelException
     */
    public static Boolean converBoolean(TemplateModel model) throws TemplateModelException {
        if (null != model) {
            if (model instanceof TemplateSequenceModel) {
                model = ((TemplateSequenceModel) model).get(0);
            }
            if (model instanceof TemplateBooleanModel) {
                return ((TemplateBooleanModel) model).getAsBoolean();
            } else if (model instanceof TemplateNumberModel) {
                return !(0 == ((TemplateNumberModel) model).getAsNumber().intValue());
            } else if (model instanceof TemplateScalarModel) {
                String temp = ((TemplateScalarModel) model).getAsString();
                if (CommonUtils.notEmpty(temp)) {
                    return Boolean.valueOf(temp);
                }
            }
        }
        return null;
    }

    /**
     * @param model
     * @return data value
     * @throws TemplateModelException
     * @throws ParseException
     */
    public static Date converDate(TemplateModel model) throws TemplateModelException, ParseException {
        if (null != model) {
            if (model instanceof TemplateSequenceModel) {
                converDate(((TemplateSequenceModel) model).get(0));
            }
            if (model instanceof TemplateDateModel) {
                return ((TemplateDateModel) model).getAsDate();
            } else if (model instanceof TemplateScalarModel) {
                String temp = StringUtils.trimToEmpty(((TemplateScalarModel) model).getAsString());
                if (DateFormatUtils.FULL_DATE_LENGTH == temp.length()) {
                    return DateFormatUtils.getDateFormat(DateFormatUtils.FULL_DATE_FORMAT_STRING).parse(temp);
                } else if (DateFormatUtils.SHORT_DATE_LENGTH == temp.length()) {
                    return DateFormatUtils.getDateFormat(DateFormatUtils.SHORT_DATE_FORMAT_STRING).parse(temp);
                } else {
                    try {
                        return new Date(Long.parseLong(temp));
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
            } else if (model instanceof TemplateNumberModel) {
                return new Date(((TemplateNumberModel) model).getAsNumber().longValue());
            }
        }
        return null;
    }
}
