package app.gridfs.web;

import app.gridfs.service.AttachmentsService;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

import static java.util.Objects.nonNull;

@Controller
@RequestMapping("/a")
public class AttachmentsController {

  @Autowired
  AttachmentsService attachmentsService;

  @Autowired
  GridFsTemplate gridFsTemplate;

  @GetMapping
  public String newAttachment(Model model) {
    model.addAttribute("items", attachmentsService.listAllFiles());
    return "a/new.html";
  }

  @PostMapping
  public String handleFileUpload(@RequestParam("file1") MultipartFile file1, RedirectAttributes redirectAttributes) throws IOException {
    attachmentsService.storeInGridFS(file1);
    redirectAttributes.addFlashAttribute("message",
            "File successfully uploaded " + file1.getOriginalFilename() + "!");
    return "redirect:/a";
  }

  @GetMapping("/download")
  public ResponseEntity<Resource> downloadFile(@RequestParam String id) throws IOException {
    GridFSFile item = attachmentsService.findOneById(id);
    if (nonNull(item)) {
      HttpHeaders headers = new HttpHeaders();
      headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
      headers.add("Pragma", "no-cache");
      headers.add("Expires", "0");
      headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + item.getFilename() + "\"");

      return ResponseEntity.ok()
              .headers(headers)
              .contentLength(item.getLength())
              .contentType(MediaType.parseMediaType("application/octet-stream"))
              .body(gridFsTemplate.getResource(item));
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/delete")
  public String delete(@RequestParam String id, RedirectAttributes redirectAttributes) {
    attachmentsService.deleteAttachments(id);
    redirectAttributes.addFlashAttribute("message", "File successfully deleted");
    return "redirect:/a";
  }

}
