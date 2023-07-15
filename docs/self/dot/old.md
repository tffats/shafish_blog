## controller
``` java
package cn.shafish.controller;

import java.util.List;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cn.shafish.entity.File;
import cn.shafish.service.FileService;

 /**
 * 文件列表;(file)表控制层
 * @author : shafish
 * @date : 2023-6-18
 */
@Api(tags = "文件列表对象功能接口")
@RestController
@RequestMapping("/file")
public class FileController{
    @Autowired
    private FileService fileService;
    
    /** 
     * 通过ID查询单条数据 
     *
     * @param id 主键
     * @return 实例对象
     */
    @ApiOperation("通过ID查询单条数据")
    @GetMapping("{id}")
    public ResponseEntity<File> queryById(String id){
        return ResponseEntity.ok(fileService.queryById(id));
    }
    
    /** 
     * 分页查询
     *
     * @param file 筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @ApiOperation("分页查询")
    @GetMapping
    public ResponseEntity<PageImpl<File>> paginQuery(File file, PageRequest pageRequest){
        //1.分页参数
        long current = pageRequest.getPageNumber();
        long size = pageRequest.getPageSize();
        //2.分页查询
        /*把Mybatis的分页对象做封装转换，MP的分页对象上有一些SQL敏感信息，还是通过spring的分页模型来封装数据吧*/
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<File> pageResult = fileService.paginQuery(file, current,size);
        //3. 分页结果组装
        List<File> dataList = pageResult.getRecords();
        long total = pageResult.getTotal();
        PageImpl<File> retPage = new PageImpl<File>(dataList,pageRequest,total);
        return ResponseEntity.ok(retPage);
    }
    
    /** 
     * 新增数据
     *
     * @param file 实例对象
     * @return 实例对象
     */
    @ApiOperation("新增数据")
    @PostMapping
    public ResponseEntity<File> add(File file){
        return ResponseEntity.ok(fileService.insert(file));
    }
    
    /** 
     * 更新数据
     *
     * @param file 实例对象
     * @return 实例对象
     */
    @ApiOperation("更新数据")
    @PutMapping
    public ResponseEntity<File> edit(File file){
        return ResponseEntity.ok(fileService.update(file));
    }
    
    /** 
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @ApiOperation("通过主键删除数据")
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(String id){
        return ResponseEntity.ok(fileService.deleteById(id));
    }
}
```

## service
``` java
package cn.shafish.controller;

import cn.shafish.model.convertq.FileQueryToDto;
import cn.shafish.entity.File;
import cn.shafish.http.RestResult;
import cn.shafish.model.dto.FileDto;
import cn.shafish.model.query.FileQuery;
import cn.shafish.service.FileService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

 /**
 * 文件列表;(file)表控制层
 * @author : shafish
 * @date : 2023-6-18
 */
@Api(tags = "文件列表功能接口")
@RestController
@RequestMapping("/file")
public class FileController{
    @Autowired
    private FileService fileService;
    
    @PostMapping("/add")
    public RestResult<Object> add(@RequestBody File file) {
        boolean add = fileService.add(file);
        return new RestResult(add, "");
    }
    
    @GetMapping("/get")
    public RestResult<Object> get(@RequestBody FileQuery fileQuery) {
        FileDto byId = fileService.getById(fileQuery.getId());
        return new RestResult(true, byId);
    }
    
    @GetMapping("/queryBy")
    public RestResult<Object> getBy(@RequestBody FileQuery fileQuery) {
        List<FileDto> aria2Files = fileService.queryBy(fileQuery);
        return new RestResult(true, aria2Files);
    }
    
    @GetMapping("/queryByPage")
    public RestResult<Object> queryByPage(@RequestBody FileQuery fileQuery) {
        IPage<FileDto> aria2FileIPage = fileService.queryByPage(fileQuery);
        return new RestResult(true, aria2FileIPage);
    }
    
    @PostMapping("/updateBy")
    public RestResult<Object> updateBy(@RequestBody FileQuery fileQuery) {
        FileDto aria2FileDto = Aria2FileQueryToDto.INSTANCE.toAria2FileDto(fileQuery);
        boolean aria2File = fileService.updateBy(aria2FileDto);
        return new RestResult(aria2File, "");
    }
    
    @PostMapping("/deleteById")
    public RestResult<Object> deleteById(@RequestBody FileQuery fileQuery) {
        boolean aria2File = fileService.deleteById(fileQuery.getId());
        return new RestResult(aria2File, "");
    }
    
}
```