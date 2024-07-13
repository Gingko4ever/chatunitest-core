package zju.cst.aces.api.impl.obfuscator.frame;

import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * SymbolFrame 类用于表示一个类中的符号框架，包括字段定义、变量定义、方法定义等。
 */
@Data
public class SymbolFrame {
    /**
     * 类名。
     */
    private String className;
    /**
     * 父类名。
     */
    private String superName;
    /**
     * 接口列表。
     */
    private List<String> interfaces;
    /**
     * 字段定义集合。
     */
    private Set<Symbol> fieldDef = new HashSet<>();
    /**
     * 字段使用集合。
     */
    private Set<Symbol> fieldUse = new HashSet<>();
    /**
     * 变量定义集合。
     */
    private Set<Symbol> varDef = new HashSet<>();
    /**
     * 变量使用集合。
     */
    private Set<Symbol> varUse = new HashSet<>();
    /**
     * 方法定义集合。
     */
    private Set<Symbol> methodDef = new HashSet<>();
    /**
     * 方法使用集合。
     */
    private Set<Symbol> methodUse = new HashSet<>();

    /**
     * 添加一个字段定义符号到集合中。
     *
     * @param symbol 要添加的符号
     */
    public void addFieldDef(Symbol symbol) {
        fieldDef.add(symbol);
    }

    /**
     * 添加一个字段使用符号到集合中。
     *
     * @param symbol 要添加的符号
     */
    public void addFieldUse(Symbol symbol) {
        fieldUse.add(symbol);
    }

    /**
     * 添加一个变量定义符号到集合中。
     *
     * @param symbol 要添加的符号
     */
    public void addVarDef(Symbol symbol) {
        varDef.add(symbol);
    }

    /**
     * 添加一个变量使用符号到集合中。
     *
     * @param symbol 要添加的符号
     */
    public void addVarUse(Symbol symbol) {
        varUse.add(symbol);
    }

    /**
     * 添加一个方法定义符号到集合中。
     *
     * @param symbol 要添加的符号
     */
    public void addMethodDef(Symbol symbol) {
        methodDef.add(symbol);
    }

    /**
     * 添加一个方法使用符号到集合中。
     *
     * @param symbol 要添加的符号
     */
    public void addMethodUse(Symbol symbol) {
        methodUse.add(symbol);
    }

    /**
     * 合并另一个符号框架到当前符号框架中。
     *
     * @param frame 要合并的符号框架
     */
    public void merge(SymbolFrame frame) {
        if (frame == null) {
            return;
        }
        if (frame.fieldDef != null) {
            fieldDef.addAll(frame.fieldDef);
        }
        if (frame.fieldUse != null) {
            fieldUse.addAll(frame.fieldUse);
        }
        if (frame.varDef != null) {
            varDef.addAll(frame.varDef);
        }
        if (frame.varUse != null) {
            varUse.addAll(frame.varUse);
        }
        if (frame.methodDef != null) {
            methodDef.addAll(frame.methodDef);
        }
        if (frame.methodUse != null) {
            methodUse.addAll(frame.methodUse);
        }
    }

    /**
     * 根据 groupIds 过滤符号，移除不属于这些组的符号。
     *
     * @param groupIds 组 ID 列表
     */
    public void filterSymbolsByGroupId(List<String> groupIds) {
        if (groupIds == null || groupIds.isEmpty()) {
            return;
        }
        List<String> targets = groupIds.stream().map(id -> id.replace(".", "/")).collect(Collectors.toList());
        className = isClassInGroup(className, groupIds) ? className.substring(className.lastIndexOf("/") + 1) : null;
        superName = isClassInGroup(superName, groupIds) ? superName.substring(superName.lastIndexOf("/") + 1) : null;
        for (int i = 0; i < interfaces.size(); i++) {
            String interfaceName = interfaces.get(i);
            if (isClassInGroup(interfaceName, groupIds)) {
                interfaces.set(i, interfaceName.substring(interfaceName.lastIndexOf("/") + 1));
            } else {
                interfaces.remove(i);
                i--;
            }
        }
        fieldDef.removeIf(symbol -> !symbol.isInGroup(targets));
        fieldUse.removeIf(symbol -> !symbol.isInGroup(targets));
        varDef.removeIf(symbol -> !symbol.isInGroup(targets));
        varUse.removeIf(symbol -> !symbol.isInGroup(targets));
        methodDef.removeIf(symbol -> !symbol.isInGroup(targets));
        methodUse.removeIf(symbol -> !symbol.isInGroup(targets));
    }

    /**
     * 生成混淆名称集合。
     *
     * @param groupIds 组 ID 列表
     * @return 混淆名称集合
     */
    public Set<String> toObNames(List<String> groupIds) {
        Set<String> obNames = new HashSet<>();
        if (className != null) {
            obNames.add(className);
        }
        if (superName != null) {
            obNames.add(superName);
        }
        if (interfaces != null) {
            obNames.addAll(interfaces);
        }
        obNames.addAll(fieldDef.stream().map(Symbol::getName).collect(Collectors.toSet()));
        obNames.addAll(fieldDef.stream().map(s -> splitTypeName(s.getOwner(), groupIds)).collect(Collectors.toSet()));
        obNames.addAll(fieldDef.stream().map(s -> splitTypeName(s.getType(), groupIds)).collect(Collectors.toSet()));

        obNames.addAll(fieldUse.stream().map(Symbol::getName).collect(Collectors.toSet()));
        obNames.addAll(fieldUse.stream().map(s -> splitTypeName(s.getOwner(), groupIds)).collect(Collectors.toSet()));
        obNames.addAll(fieldUse.stream().map(s -> splitTypeName(s.getType(), groupIds)).collect(Collectors.toSet()));

        obNames.addAll(varDef.stream().map(Symbol::getName).collect(Collectors.toSet()));
        obNames.addAll(varDef.stream().map(s -> splitTypeName(s.getOwner(), groupIds)).collect(Collectors.toSet()));
        obNames.addAll(varDef.stream().map(s -> splitTypeName(s.getType(), groupIds)).collect(Collectors.toSet()));

        obNames.addAll(varUse.stream().map(Symbol::getName).collect(Collectors.toSet()));
        obNames.addAll(varUse.stream().map(s -> splitTypeName(s.getOwner(), groupIds)).collect(Collectors.toSet()));
        obNames.addAll(varUse.stream().map(s -> splitTypeName(s.getType(), groupIds)).collect(Collectors.toSet()));

        obNames.addAll(methodDef.stream().map(Symbol::getName).collect(Collectors.toSet()));
        obNames.addAll(methodDef.stream().map(s -> splitTypeName(s.getOwner(), groupIds)).collect(Collectors.toSet()));
        obNames.addAll(methodDef.stream().map(s -> splitTypeName(s.getType(), groupIds)).collect(Collectors.toSet()));

        obNames.addAll(methodUse.stream().map(Symbol::getName).collect(Collectors.toSet()));
        obNames.addAll(methodUse.stream().map(s -> splitTypeName(s.getOwner(), groupIds)).collect(Collectors.toSet()));
        obNames.addAll(methodUse.stream().map(s -> splitTypeName(s.getType(), groupIds)).collect(Collectors.toSet()));
        obNames.remove("");
        return obNames;
    }

    /**
     * 拆分类型名称，获取类名或成员名。
     *
     * @param type     类型描述
     * @param groupIds 组 ID 列表
     * @return 拆分后的名称
     */
    public String splitTypeName(String type, List<String> groupIds) {
        if (type == null || type.isEmpty() || !isClassInGroup(type, groupIds)) {
            return "";
        }
        String[] parts = type.split("/");
        String ret = parts[parts.length - 1];
        if (ret.contains("$")) {
            ret = ret.substring(ret.lastIndexOf("$") + 1);
        }
        if (ret.contains(";")) {
            ret = ret.substring(0, ret.indexOf(";"));
        }
        return ret;
    }

    /**
     * 判断全类名是否属于指定组。
     *
     * @param fullClassName 要判断的全类名
     * @param groupIds      组 ID 列表
     * @return 如果属于指定组返回 true，否则返回 false
     */
    public static boolean isClassInGroup(String fullClassName, List<String> groupIds) {
        for (String gid : groupIds) {
            if (fullClassName.contains(gid.replace(".", "/"))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符串是否包含指定组的 ID。
     *
     * @param str      要判断的字符串
     * @param groupIds 组 ID 列表
     * @return 如果字符串包含指定组的 ID 返回 true，否则返回 false
     */
    public static boolean isInGroup(String str, List<String> groupIds) {
        for (String gid : groupIds) {
            if (str.contains(gid)) {
                return true;
            }
        }
        return false;
    }

}
