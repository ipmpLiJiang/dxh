package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.system.dao.CatalogMapper;
import cc.mrbird.febs.system.domain.Catalog;
import cc.mrbird.febs.system.service.ICatalogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author MrBird
 */
@Service
public class CatalogServiceImpl extends ServiceImpl<CatalogMapper, Catalog> implements ICatalogService {

    public List<Catalog> findCatalog(Integer employeeId) {
        List<Catalog>rootCatalog=findRootCatalog();
        LambdaQueryWrapper<Catalog> queryWrapper = new LambdaQueryWrapper<>();
        if (employeeId!=null) {
            queryWrapper.eq(Catalog::getEmployeeid, employeeId);
            List<Catalog>catalogList= baseMapper.selectList(queryWrapper);
//            catalogList.addAll(rootCatalog);
            return toTree(rootCatalog,catalogList);
        } else {
            return new ArrayList<>();
        }
    }

    public List<Catalog> findRootCatalog(){
        LambdaQueryWrapper<Catalog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Catalog::getParentid, 0);

        return this.baseMapper.selectList(queryWrapper);
    }

    public void createCatalog(List<Catalog> catalogList,String employeeid){
        this.deleteCatalogByEmployeeid(employeeid);
        for(Catalog c : catalogList){
            int no=1;
            for(Catalog children : c.getChildren()){
                if(StringUtils.isNotBlank(children.getSername())) {

                }
                children.setParentid(c.getId());
                children.setEmployeeid(c.getEmployeeid());
                children.setSecondno(no++);
                this.save(children);
            }
        }
    }

    public void updateCatalog(Catalog catalog){
        this.updateById(catalog);
    }

    public void deleteCatalog(String catalogId){
        this.baseMapper.deleteById(catalogId);
    }

    public List<Catalog> toTree(List<Catalog> parentList,List<Catalog> catalogList){
        List<Catalog> trees = new ArrayList<>();
        List<Catalog> query = new ArrayList<>();
        parentList.sort(Comparator.comparing(Catalog::getSecondno));
        for (Catalog parent : parentList) {
            query = catalogList.stream().filter(s->s.getParentid().equals(parent.getId())).collect(Collectors.toList());
            query.sort(Comparator.comparing(Catalog::getSecondno));
            parent.setChildren(query);
            trees.add(parent);
        }
        return trees;
    }

    public List<Catalog> toTree(List<Catalog> catalogList){
        List<Catalog> trees = new ArrayList<>();
        for(Catalog c: catalogList){
            boolean foundParent = false;
            for (Catalog parent: catalogList){
                if(c.getParentid()!=null && c.getParentid().equals(parent.getId())){
                    parent.getChildren().add(c);
                    foundParent = true;
                    break;
                }
            }
            if (!foundParent) {
                trees.add(c);
            }
        }
        return trees.stream().sorted(Comparator.comparing(Catalog::getSecondno)).collect(Collectors.toList());
    }

    private void deleteCatalogByEmployeeid(String employeeid){
        this.baseMapper.delete(new LambdaQueryWrapper<Catalog>().eq(Catalog::getEmployeeid,employeeid));
    }


}
