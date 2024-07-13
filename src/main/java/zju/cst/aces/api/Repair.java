package zju.cst.aces.api;

import java.io.IOException;

/**
 * Repair 接口定义了代码修复的方法。
 */
public interface Repair {

    /**
     * 基于规则的代码修复方法。
     * 
     * @param code 需要修复的代码。
     * @return 修复后的代码。
     */
    String ruleBasedRepair(String code);

    /**
     * 基于语言模型的代码修复方法。
     * 
     * @param code 需要修复的代码。
     * @return 修复后的代码。
     */
    String LLMBasedRepair(String code);

    /**
     * 基于语言模型的代码修复方法，允许指定修复轮数。
     * 
     * @param code   需要修复的代码。
     * @param rounds 修复的轮数。
     * @return 修复后的代码。
     */
    String LLMBasedRepair(String code, int rounds);

}
