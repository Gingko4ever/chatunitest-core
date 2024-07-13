package zju.cst.aces.api.impl.obfuscator.frame;

import lombok.Data;

import java.util.List;

/**
 * 符号类表示混淆框架中的符号细节，包括符号名称、所有者、类型以及在源代码中的行号。
 */
@Data
public class Symbol {
    /**
     * 符号的名称。
     */
    private String name;
    /**
     * 符号的所有者，通常是包含该符号的类或包。
     */
    private String owner;
    /**
     * 符号的类型，可以是类、方法、字段等。
     */
    private String type;
    /**
     * 符号在源代码中定义的行号。
     */
    private Integer lineNum;

    /**
     * 使用指定的详细信息构造一个新的符号。
     *
     * @param name  符号的名称
     * @param owner 符号的所有者
     * @param type  符号的类型
     * @param line  符号在源代码中定义的行号
     */
    public Symbol(String name, String owner, String type, Integer line) {
        this.name = name;
        this.owner = owner;
        this.type = type;
        this.lineNum = line;
    }

    /**
     * 判断符号是否属于列表中指定的任何组。
     *
     * @param groupIds 要检查的组标识符列表
     * @return 如果符号的所有者或类型包含任何一个组标识符，则返回true
     */
    public boolean isInGroup(List<String> groupIds) {
        for (String gid : groupIds) {
            if (owner.contains(gid) || type.contains(gid)) {
                return true;
            }
        }
        return false;
    }
}
