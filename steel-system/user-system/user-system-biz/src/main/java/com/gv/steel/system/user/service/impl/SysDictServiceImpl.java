package com.gv.steel.system.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gv.steel.common.core.enums.DictTypeEnum;
import com.gv.steel.common.core.util.MsgUtils;
import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.user.constant.ErrorCodeConstants;
import com.gv.steel.system.user.dao.SysDictDao;
import com.gv.steel.system.user.dao.SysDictItemDao;
import com.gv.steel.system.user.entity.SysDict;
import com.gv.steel.system.user.entity.SysDictItem;
import com.gv.steel.system.user.service.SysDictService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * 字典表
 */
@Service
@Transactional
@RequiredArgsConstructor
public class SysDictServiceImpl extends BaseServiceImpl<SysDictDao, SysDict> implements SysDictService {

    private final SysDictItemDao sysDictItemDao;

    /**
     * 根据ID 删除字典
     *
     * @param id 字典ID
     * @return
     */
    @Override
    public boolean removeDict(Long id) {
        SysDict dict = this.getById(id);
        // 系统内置
        Assert.state(!DictTypeEnum.SYSTEM.getType().equals(dict.getSystemFlag()),
                MsgUtils.getMessage(ErrorCodeConstants.SYS_DICT_DELETE_SYSTEM));
        baseDao.deleteById(id);
        return sysDictItemDao.delete(Wrappers.<SysDictItem>lambdaQuery().eq(SysDictItem::getDictId, id)) > 0;
    }

    /**
     * 更新字典
     *
     * @param dict 字典
     * @return
     */
    @Override
    public boolean updateDict(SysDict dict) {
        SysDict sysDict = this.getById(dict.getId());
        // 系统内置
        Assert.state(!DictTypeEnum.SYSTEM.getType().equals(sysDict.getSystemFlag()),
                MsgUtils.getMessage(ErrorCodeConstants.SYS_DICT_UPDATE_SYSTEM));
        return this.updateById(dict);
    }

    @Override
    public boolean clearDictCache() {
        return true;
    }

}
