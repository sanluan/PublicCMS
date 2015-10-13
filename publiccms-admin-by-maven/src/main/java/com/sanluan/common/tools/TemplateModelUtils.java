package com.sanluan.common.tools;

import static com.sanluan.common.constants.CommonConstants.FULL_DATE_FORMAT;
import static com.sanluan.common.constants.CommonConstants.FULL_DATE_LENGTH;
import static com.sanluan.common.constants.CommonConstants.SHORT_DATE_FORMAT;
import static com.sanluan.common.constants.CommonConstants.SHORT_DATE_LENGTH;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.split;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

import java.text.ParseException;
import java.util.Date;

import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateDateModel;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;
import freemarker.template.TemplateScalarModel;
import freemarker.template.TemplateSequenceModel;

/**
 * 
 * TemplateModelUtils 模板数据模型帮助泪
 *
 */
public class TemplateModelUtils {
    /**
     * @param model
     * @return
     * @throws TemplateModelException
     */
    public static String converString(TemplateModel model) throws TemplateModelException {
        if (null != model) {
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
    public static TemplateHashModel converMap(TemplateModel model) throws TemplateModelException {
        if (null != model) {
            if (model instanceof TemplateHashModelEx) {
                return (TemplateHashModelEx) model;
            } else if (model instanceof TemplateHashModel) {
                return (TemplateHashModel) model;
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
            if (model instanceof TemplateNumberModel) {
                return ((TemplateNumberModel) model).getAsNumber().intValue();
            } else if (model instanceof TemplateScalarModel) {
                String s = ((TemplateScalarModel) model).getAsString();
                if (isNotBlank(s)) {
                    try {
                        return Integer.parseInt(s);
                    } catch (NumberFormatException e) {
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
            if (model instanceof TemplateNumberModel) {
                return ((TemplateNumberModel) model).getAsNumber().shortValue();
            } else if (model instanceof TemplateScalarModel) {
                String s = ((TemplateScalarModel) model).getAsString();
                if (isNotBlank(s)) {
                    try {
                        return Short.parseShort(s);
                    } catch (NumberFormatException e) {
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
            if (model instanceof TemplateNumberModel) {
                return ((TemplateNumberModel) model).getAsNumber().longValue();
            } else if (model instanceof TemplateScalarModel) {
                String s = ((TemplateScalarModel) model).getAsString();
                if (isNotBlank(s)) {
                    try {
                        return Long.parseLong(s);
                    } catch (NumberFormatException e) {
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
            if (model instanceof TemplateNumberModel) {
                return ((TemplateNumberModel) model).getAsNumber().doubleValue();
            } else if (model instanceof TemplateScalarModel) {
                String s = ((TemplateScalarModel) model).getAsString();
                if (isNotBlank(s)) {
                    try {
                        return Double.parseDouble(s);
                    } catch (NumberFormatException e) {
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
        } else {
            String str = converString(model);
            if (isNotBlank(str)) {
                return split(str, ',');
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
            if (model instanceof TemplateBooleanModel) {
                return ((TemplateBooleanModel) model).getAsBoolean();
            } else if (model instanceof TemplateNumberModel) {
                return !(0 == ((TemplateNumberModel) model).getAsNumber().intValue());
            } else if (model instanceof TemplateScalarModel) {
                String temp = ((TemplateScalarModel) model).getAsString();
                if (isNotBlank(temp)) {
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
     */
    public static Date converDate(TemplateModel model) throws TemplateModelException {
        if (null != model) {
            if (model instanceof TemplateDateModel) {
                return ((TemplateDateModel) model).getAsDate();
            } else if (model instanceof TemplateScalarModel) {
                String temp = trimToEmpty(((TemplateScalarModel) model).getAsString());
                try {
                    if (FULL_DATE_LENGTH == temp.length()) {
                        return FULL_DATE_FORMAT.parse(temp);
                    } else if (SHORT_DATE_LENGTH == temp.length()) {
                        return SHORT_DATE_FORMAT.parse(temp);
                    }
                } catch (ParseException e) {
                }
            }
        }
        return null;
    }
}
