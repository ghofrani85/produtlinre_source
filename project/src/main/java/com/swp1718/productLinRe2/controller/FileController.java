package com.swp1718.productLinRe2.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileUploadBase.SizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.MultipartProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.swp1718.productLinRe2.Application;
import com.swp1718.productLinRe2.controller.service.IUserService;
import com.swp1718.productLinRe2.controller.service.TrackingService;
import com.swp1718.productLinRe2.controller.setup.ConfigData;
import com.swp1718.productLinRe2.database.AssetAccess;
import com.swp1718.productLinRe2.database.UserAccess;
import com.swp1718.productLinRe2.model.Asset;
import com.swp1718.productLinRe2.model.AudioAsset;
import com.swp1718.productLinRe2.model.OtherAsset;
import com.swp1718.productLinRe2.model.PictureAsset;
import com.swp1718.productLinRe2.model.TextAsset;
import com.swp1718.productLinRe2.model.User;
import com.swp1718.productLinRe2.model.VideoAsset;
import com.swp1718.productLinRe2.model.XMLAsset;

@Controller
public class FileController {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	@Autowired
	private AssetAccess assetAccess;

	@Autowired
	private IUserService userService;

	@Autowired
	private UserAccess userAccess;

	@Autowired
	private TrackingService trackingService;

	@Autowired
	private ConfigData configData;

	@Autowired
	private MyResourceHttpRequestHandler handler;

	/**
	 * Handles uploading of a new Asset
	 * 
	 * @param title
	 *            Title of the new Asset
	 * @param description
	 *            Description of the new Asset
	 * @param file
	 *            File that is uploaded
	 * @param URL
	 *            Alternative to file, a url pointing to a file
	 * @param featureID
	 *            ID of the feature that is currently openend(null if none opened)
	 * @param httpRequest
	 *            Request Object for the View
	 * @return redirect to assetview of new asset
	 * @throws IOException
	 *             when the file could not be read
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/fileUpload")
	public String handleFileUpload(@RequestParam("title") String title, @RequestParam("description") String description,
			@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "url", required = false) String URL, @RequestParam("featureId") Integer featureID,
			HttpServletRequest httpRequest) throws IOException {

		// Check URL/File for integrity
		if (file != null) {

			if (file.isEmpty() || file.getOriginalFilename().isEmpty()) {
				return "redirect:feature?id=" + featureID + "&invalidFileError=true";
			}

		} else if (URL != null) {

			try {
				UrlResource urlTarget = new UrlResource(URL);

				if (!urlTarget.exists() || !urlTarget.isReadable() || urlTarget.contentLength() <= 0) {
					return "redirect:feature?id=" + featureID + "&invalidURLError=true";
				}

			} catch (MalformedURLException e) {
				return "redirect:feature?id=" + featureID + "&invalidURLError=true";
			}

		} else {
			return "redirect:feature?id=" + featureID + "&invalidFileError=true";
		}

		// Retrieve currently active User
		org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		User currentUser = userService.loadUser(user.getUsername());

		// Check if title already exists in users lib:
		List<Asset> assetList = assetAccess.selectAssets();

		for (Asset a : assetList) {

			if (a.getTitle().equals(title)) {
				// Other Asset has same Title, throw Error

				return "redirect:feature?id=" + featureID + "&AssetDuplicateTitleError=true&title=" + title;
			}

			// }
		}

		Asset asset = null;

		if (file != null) {
			asset = buildAssetFromFile(title, description, file, httpRequest);
			if (asset == null) {
				return "redirect:feature?id=" + featureID + "&DailyUploadVolumeReached="
						+ convertSizeToString(configData.getDailyUploadLimitInBytes());
			}

		} else if (URL != null) {
			asset = buildAssetFromURL(title, description, URL, httpRequest);
		} else {
			return "redirect:error";
		}

		int id = asset.getId();

		if (featureID != null) {
			// Redirect to AssetView with specific Feature
			return "redirect:asset?id=" + id + "&featureId=" + featureID;
		} else {
			// Redirect to AssetView without feature information
			return "redirect:asset?id=" + id;
		}
	}

	/**
	 * Controller to handle requests for saved asset files
	 * 
	 * @param subFolder
	 *            Which subfolder the asset is saved in
	 * @param fileName
	 *            Name of the File
	 * @param format
	 *            Format of the file
	 * @param response
	 *            Response Object for the Request
	 * @param request
	 *            Request Object for the Request
	 * @throws IOException
	 *             When File could not be read
	 * @throws ServletException
	 *             When the servlet encounters an Error
	 */
	@RequestMapping(value = "files/{sub_folder}/{file_name}.{format}", method = RequestMethod.GET)
	@ResponseBody
	public void getFile(@PathVariable("sub_folder") String subFolder, @PathVariable("file_name") String fileName,
			@PathVariable("format") String format, HttpServletResponse response, HttpServletRequest request)
			throws IOException, ServletException {
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		FileSystemResource resource = new FileSystemResource(
				Asset.assetPath + "/" + subFolder + "/" + fileName + "." + format);

		request.setAttribute(MyResourceHttpRequestHandler.ATTR_FILE, resource.getFile());
		handler.handleRequest(request, response);
	}

	/**
	 * Save the file to local storage
	 * 
	 * @param file
	 *            the file to save
	 * @return a string representation of an URI pointing to the File
	 * @throws IOException
	 *             when the file could not be saved
	 */
	public String saveFile(MultipartFile file) throws IOException {

		int subFolderIndex = 0;
		String filename = file.getOriginalFilename();
		filename = filename.replaceAll("[\\\\/:*?\"<>|]", "_");

		Path path = Paths.get(Asset.assetPath + "/" + "sub" + subFolderIndex + "/" + filename);

		// Creates Directory, if it not already exists
		Files.createDirectories(Paths.get(Asset.assetPath + "/" + "sub" + subFolderIndex));

		// Check if parameter are valid
		if (path == null || file.isEmpty()) {
			return null;
		}

		// Check if file already exists, change sub until there is no copy
		while (path.toFile().isFile()) {
			subFolderIndex++;

			// Creates Directory, if it not already exists
			Files.createDirectories(Paths.get(Asset.assetPath + "/" + "sub" + subFolderIndex));

			path = Paths.get(Asset.assetPath + "/" + "sub" + subFolderIndex + "/" + filename);
		}

		byte[] bytes;

		try {
			bytes = file.getBytes();
			Files.write(path, bytes);

		} catch (IOException e) {

			e.printStackTrace();
			throw new IOException();
		}

		return "files/sub" + subFolderIndex + "/" + filename;
	}

	/**
	 * Converts the size(in bytes) into a readable string,the unit will be changed
	 * to KB, MB, GB when size exceeds 1000 units
	 * 
	 * @param size,
	 *            the size which should be converted
	 * @return A string representing the filesize in an appropriate unit
	 */
	public String convertSizeToString(long size) {

		int magnitude = 0;
		while (size > 1000 && magnitude < 3) {
			size = size / 1000;
			magnitude++;
		}

		String fileSize = "";

		switch (magnitude) {
		case 0:
			fileSize = size + "B";
			break;
		case 1:
			fileSize = size + "KB";
			break;
		case 2:
			fileSize = size + "MB";
			break;
		case 3:
			fileSize = size + "GB";
			break;
		default:
			fileSize = "";
		}

		return fileSize;

	}

	/**
	 * Retrieve the filetype of a file
	 * 
	 * @param filename
	 *            Name of the file
	 * @return The Filetype as string, lower-case, without the "."
	 */
	public String retrieveFileFormat(String filename) {
		String format = "none";

		// Retrieve formatname by splitting filename
		if (filename.contains(".")) {
			String[] splitted = filename.split("\\.");
			format = splitted[splitted.length - 1];
		}

		return format.toLowerCase();
	}

	/**
	 * Creates and saves an asset using an uploaded File
	 * 
	 * @param title
	 *            Title of the new Asset
	 * @param description
	 *            Description of the new asset
	 * @param file
	 *            The file which the asset represents
	 * @param httpRequest
	 *            Request Object for this Request
	 * @return The created asset object, null if dailyUploadVolume was reached
	 * @throws IOException When file could not be saved
	 */
	public Asset buildAssetFromFile(String title, String description, MultipartFile file,
			HttpServletRequest httpRequest) throws IOException {

		String filename = file.getOriginalFilename();
		filename = filename.replaceAll("[\\\\/:*?\"<>|]", "_");

		String fileFormat = retrieveFileFormat(filename);

		String fileSize = convertSizeToString(file.getSize());

		// Retrieve currently active User
		org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		User currentUser = userService.loadUser(user.getUsername());

		// Check if DailyUploadVolumeLimit is reached, admin has infinite volume
		if (!currentUser.getRoles().contains("ROLE_ADMIN")
				&& (currentUser.getDailyUploadVolume() + file.getSize() > configData.getDailyUploadLimitInBytes())) {
			return null;
		}

		currentUser.setDailyUploadVolume(currentUser.getDailyUploadVolume() + (int) file.getSize());
		currentUser.setTotalDataVolume(currentUser.getTotalDataVolume() + (int) file.getSize());
		userAccess.saveUser(currentUser);

		// Save file to asset_folder, retrieve relative link to file-system
		String urlToFile = saveFile(file);

		Asset asset;

		if (fileFormat.equals("png") || fileFormat.equals("jpg") || fileFormat.equals("bmp")
				|| fileFormat.equals("gif")) {
			asset = new PictureAsset(null, title, description, urlToFile);
		} else if (fileFormat.equals("mp4")) {
			asset = new VideoAsset(null, title, description, urlToFile);
		} else if (fileFormat.equals("mp3")) {
			asset = new AudioAsset(null, title, description, urlToFile);
		} else if (fileFormat.equals("xml")) {
			asset = new XMLAsset(null, title, description, urlToFile);
		} else if (fileFormat.equals("txt") || fileFormat.equals("html")) {
			asset = new TextAsset(null, title, description, urlToFile);
		} else {
			asset = new OtherAsset(null, title, description, urlToFile);
		}

		String userID = currentUser.getId().toString();

		// Create Metadata
		Map<String, String> metadata = new HashMap<String, String>();

		metadata.put("Filename", filename);
		metadata.put("FileFormat", fileFormat);
		metadata.put("Size", fileSize);
		metadata.put("Uploaded by", userID);
		metadata.put("Uploaded on", new Date().toString());

		asset.setMetadata(metadata);

		asset = trackingService.trackCreatedAsset(asset, httpRequest);

		asset = assetAccess.saveAsset(asset);

		return asset;
	}

	
	/**
	 * Creates Asset from given URL pointing to file
	 * @param title Title of the new asset
	 * @param description Description of the new asset
	 * @param URL The url pointing to the file
	 * @param httpRequest Request Object
	 * @return The Asset created, null if there was an error
	 * @throws IOException When the file could not be loaded from URL
	 */
	public Asset buildAssetFromURL(String title, String description, String URL, HttpServletRequest httpRequest)
			throws IOException {

		UrlResource urlTarget = new UrlResource(URL);

		String filename = urlTarget.getFilename();
		String fileFormat = retrieveFileFormat(filename);
		String fileSize = convertSizeToString(urlTarget.contentLength());

		// Retrieve currently active User
		org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		User currentUser = userService.loadUser(user.getUsername());
		String userID = currentUser.getId().toString();

		Asset asset;

		if (fileFormat.equals("png") || fileFormat.equals("jpg") || fileFormat.equals("bmp")
				|| fileFormat.equals("gif")) {
			asset = new PictureAsset(null, title, description, URL);
		} else if (fileFormat.equals("mp4")) {
			asset = new VideoAsset(null, title, description, URL);
		} else if (fileFormat.equals("mp3")) {
			asset = new AudioAsset(null, title, description, URL);
		} else if (fileFormat.equals("xml")) {
			asset = new XMLAsset(null, title, description, URL);
		} else if (fileFormat.equals("txt")) {
			asset = new TextAsset(null, title, description, URL);
		} else {
			asset = new OtherAsset(null, title, description, URL);
		}

		// Create Metadata
		Map<String, String> metadata = new HashMap<String, String>();

		metadata.put("Filename", filename);
		metadata.put("FileFormat", fileFormat);
		metadata.put("Size", fileSize);
		metadata.put("Uploaded by", userID);
		metadata.put("Uploaded on", new Date().toString());

		asset.setMetadata(metadata);

		asset = trackingService.trackCreatedAsset(asset, httpRequest);

		asset = assetAccess.saveAsset(asset);

		return asset;
	}

	/**
	 * 
	 * Handler for making partial file request possible
	 * is needed to make video/audio in browser "skippable"
	 *
	 */
	@Component
	static final class MyResourceHttpRequestHandler extends ResourceHttpRequestHandler {

		private static final String ATTR_FILE = MyResourceHttpRequestHandler.class.getName() + ".file";

		@Override
		protected Resource getResource(HttpServletRequest request) throws IOException {

			final File file = (File) request.getAttribute(ATTR_FILE);
			return new FileSystemResource(file);
		}
	}

}
