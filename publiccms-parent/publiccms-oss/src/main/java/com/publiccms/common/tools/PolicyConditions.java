package com.publiccms.common.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.publiccms.common.tools.PolicyConditions.MatchMode;

/**
 * One condition item
 */
class ConditionItem {
    /**
     * The condition tuple type: currently only supports Two and Three.
     */
    enum TupleType {
        Two, Three
    };

    private String name;
    private MatchMode matchMode;
    private String value;
    private TupleType tupleType;
    private long minimum;
    private long maximum;
    private String[] contain;

    public ConditionItem(String name, String value) {
        this.matchMode = MatchMode.Exact;
        this.name = name;
        this.value = value;
        this.tupleType = TupleType.Two;
    }

    public ConditionItem(String name, long min, long max) {
        this.matchMode = MatchMode.Range;
        this.name = name;
        this.minimum = min;
        this.maximum = max;
        this.tupleType = TupleType.Three;
    }

    public ConditionItem(MatchMode matchMode, String name, String value) {
        this.matchMode = matchMode;
        this.name = name;
        this.value = value;
        this.tupleType = TupleType.Three;
    }

    public ConditionItem(MatchMode matchMode, String name, String[] contain) {
        this.matchMode = matchMode;
        this.name = name;
        this.contain = contain;
        this.tupleType = TupleType.Three;
    }

    public String jsonize() {
        String jsonizedCond = null;
        switch (tupleType) {
        case Two:
            jsonizedCond = String.format("{\"%s\":\"%s\"},", name, value);
            break;
        case Three:
            switch (matchMode) {
            case Exact:
                jsonizedCond = String.format("[\"eq\",\"$%s\",\"%s\"],", name, value);
                break;
            case StartWith:
                jsonizedCond = String.format("[\"starts-with\",\"$%s\",\"%s\"],", name, value);
                break;
            case Range:
                jsonizedCond = String.format("[\"content-length-range\",%d,%d],", minimum, maximum);
                break;
            case In:
                jsonizedCond = String.format("[\"in\",\"$%s\",[\"%s\"]],", name, StringUtils.join("\",\"", contain));
                break;
            case NotIn:
                jsonizedCond = String.format("[\"not-in\",\"$%s\",[\"%s\"]],", name, StringUtils.join("\",\"", contain));
                break;
            default:
                throw new IllegalArgumentException(String.format("Unsupported match mode %s", matchMode.toString()));
            }
            break;
        }

        return jsonizedCond;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MatchMode getMatchMode() {
        return matchMode;
    }

    public void setMatchMode(MatchMode matchMode) {
        this.matchMode = matchMode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TupleType getTupleType() {
        return tupleType;
    }

    public void setTupleType(TupleType tupleType) {
        this.tupleType = tupleType;
    }

    public long getMinimum() {
        return minimum;
    }

    public void setMinimum(long minimum) {
        this.minimum = minimum;
    }

    public long getMaximum() {
        return maximum;
    }

    public void setMaximum(long maximum) {
        this.maximum = maximum;
    }
}

/**
 * Policy Conditions. This is to specify the conditions in a post request.
 */
public class PolicyConditions {
    public static final String COND_CONTENT_LENGTH_RANGE = "content-length-range";
    public static final String COND_CACHE_CONTROL = "Cache-Control";
    public static final String COND_CONTENT_TYPE = "Content-Type";
    public static final String COND_CONTENT_DISPOSITION = "Content-Disposition";
    public static final String COND_CONTENT_ENCODING = "Content-Encoding";
    public static final String COND_EXPIRES = "Expires";
    public static final String COND_KEY = "key";
    public static final String COND_ACL = "acl";
    public static final String COND_BUCKET = "bucket";
    public static final String COND_SUCCESS_ACTION_REDIRECT = "success_action_redirect";
    public static final String COND_SUCCESS_ACTION_STATUS = "success_action_status";
    public static final String COND_X_OSS_META_PREFIX = "x-amz-meta-uuid";
    public static final String COND_X_OSS_DATE = "x-amz-date";
    public static final String COND_X_OSS_ALGORITHM = "x-amz-algorithm";
    public static final String COND_X_OSS_CREDENTIAL = "x-amz-credential";
    public static final String COND_X_OSS_SERVER_SIDE_PREFIX = "x-amz-server-side-";

    private static Map<String, List<MatchMode>> _supportedMatchRules = new HashMap<String, List<MatchMode>>();
    private List<ConditionItem> _conds = new ArrayList<ConditionItem>();

    static {
        List<MatchMode> ordinaryMatchModes = new ArrayList<MatchMode>();
        ordinaryMatchModes.add(MatchMode.Exact);
        ordinaryMatchModes.add(MatchMode.StartWith);
        ordinaryMatchModes.add(MatchMode.In);
        ordinaryMatchModes.add(MatchMode.NotIn);
        List<MatchMode> specialMatchModes = new ArrayList<MatchMode>();
        specialMatchModes.add(MatchMode.Range);

        _supportedMatchRules.put(COND_CONTENT_LENGTH_RANGE, specialMatchModes);

        _supportedMatchRules.put(COND_CACHE_CONTROL, ordinaryMatchModes);
        _supportedMatchRules.put(COND_CONTENT_TYPE, ordinaryMatchModes);
        _supportedMatchRules.put(COND_CONTENT_DISPOSITION, ordinaryMatchModes);
        _supportedMatchRules.put(COND_CONTENT_ENCODING, ordinaryMatchModes);
        _supportedMatchRules.put(COND_EXPIRES, ordinaryMatchModes);

        _supportedMatchRules.put(COND_KEY, ordinaryMatchModes);
        _supportedMatchRules.put(COND_SUCCESS_ACTION_REDIRECT, ordinaryMatchModes);
        _supportedMatchRules.put(COND_SUCCESS_ACTION_STATUS, ordinaryMatchModes);
        _supportedMatchRules.put(COND_X_OSS_META_PREFIX, ordinaryMatchModes);
        _supportedMatchRules.put(COND_X_OSS_SERVER_SIDE_PREFIX, ordinaryMatchModes);
    }

    /**
     * Adds a condition item with the exact match mode.
     * 
     * @param name
     *            Condition name.
     * @param value
     *            Condition value.
     */
    public void addConditionItem(String name, String value) {
        checkMatchModes(MatchMode.Exact, name);
        _conds.add(new ConditionItem(name, value));
    }

    /**
     * Adds a condition item with specified {@link MatchMode} value.
     * 
     * @param matchMode
     *            Conditions match mode.
     * @param name
     *            Condition name.
     * @param value
     *            Condition value.
     */
    public void addConditionItem(MatchMode matchMode, String name, String value) {
        checkMatchModes(matchMode, name);
        _conds.add(new ConditionItem(matchMode, name, value));
    }

    /**
     * Adds a range match condition.
     * 
     * @param name
     *            Condition name
     * @param min
     *            Min value.
     * @param max
     *            Max value.
     */
    public void addConditionItem(String name, long min, long max) {
        if (min > max)
            throw new IllegalArgumentException(String.format("Invalid range [%d, %d].", min, max));
        _conds.add(new ConditionItem(name, min, max));
    }

    /**
     * Adds a condition item with specified {@link MatchMode} value.
     *
     * @param matchMode
     *            Conditions match mode.
     * @param name
     *            Condition name.
     * @param contain
     *            Condition contain.
     */
    public void addConditionItem(MatchMode matchMode, String name, String[] contain) {
        checkMatchModes(matchMode, name);
        _conds.add(new ConditionItem(matchMode, name.toLowerCase(), contain));
    }

    private void checkMatchModes(MatchMode matchMode, String condName) {
        if (_supportedMatchRules.containsKey(condName)) {
            List<MatchMode> mms = _supportedMatchRules.get(condName);
            if (!mms.contains(matchMode))
                throw new IllegalArgumentException(String.format("Unsupported match mode for condition item %s", condName));
        }
    }

    public String jsonize() {
        StringBuilder jsonizedConds = new StringBuilder();
        jsonizedConds.append("\"conditions\":[");
        for (ConditionItem cond : _conds)
            jsonizedConds.append(cond.jsonize());
        if (_conds.size() > 0)
            jsonizedConds.deleteCharAt(jsonizedConds.length() - 1);
        jsonizedConds.append("]");
        return jsonizedConds.toString();
    }

    /**
     * The match mode for post policy conditions
     */
    public enum MatchMode {
        Unknown, Exact, // Exact match
        StartWith, // Starts With
        Range, // The range of file size
        In, NotIn
    }
}