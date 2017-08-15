package com.publiccms.common.tools;

import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static org.apache.commons.lang3.StringUtils.split;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.publiccms.common.base.Base;

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
public class TemplateModelUtils implements Base {

    private static final String FULL_DATE_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
    private static final String SHORT_DATE_FORMAT_STRING = "yyyy-MM-dd";
    /**
     * 
     */
    public static final DateFormat FULL_DATE_FORMAT = new SimpleDateFormat(FULL_DATE_FORMAT_STRING);
    /**
     * 
     */
    public static final int FULL_DATE_LENGTH = FULL_DATE_FORMAT_STRING.length();
    /**
     * 
     */
    public static final DateFormat SHORT_DATE_FORMAT = new SimpleDateFormat(SHORT_DATE_FORMAT_STRING);
    /**
     * 
     */
    public static final int SHORT_DATE_LENGTH = SHORT_DATE_FORMAT_STRING.length();

    /**
     * @param model
     * @return
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
     * @return
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
     * @return
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
     * @return
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
        if (null != model) {
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
        if (null != model) {
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
        if (null != model) {
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
                return split(str, COMMA_DELIMITED);
            } else {
                return split(str, BLANK_SPACE);
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
        if (null != model) {
            if (model instanceof TemplateSequenceModel) {
                converDate(((TemplateSequenceModel) model).get(0));
            }
            if (model instanceof TemplateDateModel) {
                return ((TemplateDateModel) model).getAsDate();
            } else if (model instanceof TemplateScalarModel) {
                String temp = trimToEmpty(((TemplateScalarModel) model).getAsString());
                if (FULL_DATE_LENGTH == temp.length()) {
                    synchronized (FULL_DATE_FORMAT) {
                        return FULL_DATE_FORMAT.parse(temp);
                    }
                } else if (SHORT_DATE_LENGTH == temp.length()) {
                    synchronized (SHORT_DATE_FORMAT) {
                        return SHORT_DATE_FORMAT.parse(temp);
                    }
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
