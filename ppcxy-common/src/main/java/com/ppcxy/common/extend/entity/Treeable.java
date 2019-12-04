package com.ppcxy.common.extend.entity;

import java.io.Serializable;

/**
 * <p>实体实现该接口表示想要实现树结构
 * <p/>
 */
public interface Treeable<ID extends Serializable> {

    String getName();

    void setName(String name);

    /**
     * 显示的图标 大小为16×16
     */
    String getIcon();

    void setIcon(String icon);

    /**
     * 父路径
     *
     * @return
     */
    ID getParentId();

    void setParentId(ID parentId);

    /**
     * 所有父路径 如1,2,3,
     *
     * @return
     */
    String getParentIds();

    void setParentIds(String parentIds);

    /**
     * 获取 parentIds 之间的分隔符
     *
     * @return
     */
    String getSeparator();

    /**
     * 把自己构造出新的父节点路径
     *
     * @return
     */
    String makeSelfAsNewParentIds();

    /**
     * 权重 用于排序 越小越排在前边
     *
     * @return
     */
    Integer getWeight();

    void setWeight(Integer weight);

    /**
     * 是否是根节点
     *
     * @return
     */
    boolean isRoot();

    /**
     * 是否是叶子节点
     *
     * @return
     */
    boolean isLeaf();

    /**
     * 是否有孩子节点
     *
     * @return
     */
    boolean isHasChildren();

    /**
     * 根节点默认图标 如果没有默认 空即可  大小为16×16
     */
    String getRootDefaultIcon();

    /**
     * 树枝节点默认图标 如果没有默认 空即可  大小为16×16
     */
    String getBranchDefaultIcon();

    /**
     * 树叶节点默认图标 如果没有默认 空即可  大小为16×16
     */
    String getLeafDefaultIcon();
}
