# ImageMagnet (WIP)
Place the executable in a folder with images to attract similar content to its location

#### ⬇️  [Download .exe](https://github.com/Kerbonaut-TS/ImageMagnet/releases/download/v0.92/ImageMagnet.exe) <br>
#### ⬇️  [Download executable .jar](https://github.com/Kerbonaut-TS/ImageMagnet/releases/download/v0.92/ImageMagnet.jar)

The idea behind file magnet is to have a portable app that you can move from folder to folder, allowing with few clicks to move similar images to the same location.
The similarity is assessed on criteria that can be selected or excluded: 

✔️ same filenames <br>
✔️ same extensions  <br>
✔️ same timestamp <br>
⌛  same or similar content (based on RGB statistics) <br>


![image](https://github.com/user-attachments/assets/8e815d61-bb9d-4649-b07c-5ca0a517bdbe)


It was ideated to optimize a photography workflow and organize image selections,  it can also be used to organize training sets 
for machine learning image recognition.



Instructions: 

1. place the app in a folder that contains images.
2. launch the jar/exe 
4. select the folder from which you want to attract more files
3. chose similarity criterias 
4. (Most settings are pre-filled based on the folder content) 
5. press start.


### Attraction logic and similarity rules

attract files with 

- same extension
- same name (but different extension)


similar timestamp (multiple selections possible)
- same date 
- same hour 
- same minute
- same second

similar content
- Exactly same content: RGB statistics and file size match. 
- Similar but not same  content (WIP)
