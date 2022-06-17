package app.gridfs.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Map;

import static java.util.Objects.nonNull;

@Service
public class AttachmentsService {

  @Autowired
  @Getter
  GridFsOperations gridFsOperations;

  public GridFSFile storeInGridFS(MultipartFile file) throws IOException {
    return storeInGridFS(file.getInputStream(), file.getOriginalFilename(), file.getSize());
  }

  public GridFSFile storeInGridFS(InputStream fileStream, String fileName, long size) {
    return storeInGridFS(fileStream, fileName, size, null);
  }

  public GridFSFile storeInGridFS(InputStream fileStream, String fileName, long size, Map<String, Object> myMeta) {

    DBObject metadata = new BasicDBObject();
    metadata.put("name", fileName);
    metadata.put("size", size);
    metadata.put("mime", URLConnection.guessContentTypeFromName(fileName));
    if (nonNull(myMeta)) {
      metadata.putAll(myMeta);
    }

    ObjectId objectId = getGridFsOperations().store(fileStream, fileName, metadata);
    return getGridFsOperations().findOne(new Query(Criteria.where("_id").is(objectId)));
  }

  public GridFSFile findOneById(String id) {
    Query fsQuery = new Query(GridFsCriteria.where("_id").is(id));
    return getGridFsOperations().findOne(fsQuery);
  }

  ;

  public void deleteAttachments(String id) {
    getGridFsOperations().delete(new Query(GridFsCriteria.where("_id").is(id)));
  }


  public GridFSFindIterable listAllFiles() {
    return getGridFsOperations().find(new Query());
  }

  ;


}
