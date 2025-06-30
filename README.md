# FileMagnet (WIP)
Place the executable in a folder with images to attract similar content to its location

The idea behind file magnet is to have a portable app that you can move from folder to folder, allowing with few clicks to move similar images to the same location.
The similarity is assessed on criteria that can be selected or excluded: 

same filenames
same extensions 
same timestamp 
same or similar content (based on RGB statistics)

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