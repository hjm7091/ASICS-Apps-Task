### Donetop Project: File Saving Architecture & Implementation 

* * *

#### 1. Introduction

This website serves a printing business, with one of its key features being the display of design results created by graphic designers based on client requests. 
Consequently, the ability to save files is an essential requirement for this website.

#### 2. Considerations

- The default file-saving strategy is to save files to the local file system, but it should be adaptable to use AWS S3 for improved scalability.
- Multiple domains (Draft, Category, and Comment) can independently store files, so the implemented file system should be designed for reusability.

#### 3. Domain Modeling (File & Folder)

<img width="395" alt="image" src="https://github.com/donetop/donetop/assets/28583661/03a4bc95-ad9a-4d17-9576-039f40246da3">

This illustrates the association between Files and Folders. 
It establishes a one-to-many relationship, with Folders containing path information where files are saved in the local file system. 
Each file can be identified by connecting the Folder's path and the File's name (e.g., C:/donetop/storage/category/79/sample.jpg). 
Notably, the folder path contains a unique domain ID.

#### 4. Advanced Domain Modeling with Other Domains

In the case of the Draft domain, it must handle multiple folders. 
The association formed is as follows:

<img width="574" alt="image" src="https://github.com/donetop/donetop/assets/28583661/9fdafa92-ed5a-414e-b07c-084f0fe2a512">

The Draft domain can have multiple folders by saving each folder's information in the DraftFolder table. 
The folderType column is used to distinguish each folder. 
Although DraftFolder is a subtype of Folder, it needs to be maintained as a separate table due to the absence of table subtyping in MySQL.

In the Category domain, there's no need for multiple folders, so it can reference the folder directly through a one-to-one association.

<img width="398" alt="image" src="https://github.com/donetop/donetop/assets/28583661/f023c2cc-ff20-44b1-b5c2-3f29397a7ebb">

#### 5. Class Modeling

<img width="796" alt="image" src="https://github.com/donetop/donetop/assets/28583661/01f4d1ac-d315-47ba-a353-afbe80db25f1">

While this may appear complex initially, it becomes easier to understand with explanations. 
Some domains serve as container folders by implementing the FolderContainer interface. 
This interface also includes two sub-interfaces to specify how many folders each domain can have.

Now, let's look at the service class using the above classes.

<img width="712" alt="image" src="https://github.com/donetop/donetop/assets/28583661/4f65396d-6574-4c64-9043-8c833846fb41">

The StorageService interface includes methods to manage resources delivered by a client. 
The LocalStorageService class, its implementation, covers all methods for saving resources to the local file system. 
This service can handle both types (MultipleFolderContainer and SingleFolderContainer) simultaneously, eliminating the need for duplicated service code for each domain. 
It promotes code reuse for every domain.

#### 6. Accomplishments

I successfully implemented this feature with a strong object-oriented programming (OOP) concept. 
I prioritized code reusability and scalability, making it convenient to add file-saving features to new domains. 
Additionally, implementing AWSStorageService for saving resources to AWS S3 storage can be easily accomplished when needed as a replacement for LocalStorageService.
You can check the entire project at [here](https://github.com/donetop/donetop). 
