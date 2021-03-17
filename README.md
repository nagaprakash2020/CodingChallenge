# CodingChallenge

This Android project is built with MVVM architecture

Building:
  App can be installed and run by pressing the run button in Android Studio.
  
Testing:
  No UI tests are added
  Local Unit Tests:
      TransactionViewModel, RemoteRepo classes are tested using JUnit and Mockito.
      A mock implementations are provided for the class dependencies

This app is composed of 1 screen

TransactionsActivity (View)
  Responsible for displaying a list of Transactions.
ViewBinding is used to bind views from XML
            ViewModel is created using viewModels() from android-ktx library from google
            TransactionsAdapter is added to Recycler view to display the list
  Note: Have not spent time on UI. UI is kept to bare minimum.

  States 
LoadingState 
A spinner is displayed indicating user that data is being fetch
DisplayedList
Transactions list is displayed
MessageState 
A message will be displayed when there is an issue while fetching the data, or when there are no transactions to display


          
TransactionViewModel(ViewModel)
  This is dependent on CodingRepo which is constructor injected using HILT
  Responsible for fetching Transactions.
  Updates the "transactionStates" based on the results acquired from Repository
  
  
Repository
CodingRepo: This is dependent on RemoteRepo to get the transactions. RemoteRepo is constructor injected using HILT

RemoteRepo:  This is dependent on CodingServices which is a constructor injected using HILT. Returns the state of getTransactions service call.
  
  
  
          
          

Views will update the viewmodel of any interaction from the user and observe the livedata objects in viewmodels.
ViewModels will request data from Repository and update livedata objects inside it which in turn observed by the views.
Repository is gets remote data from RemoteRepository
Remote repository makes a network call and gets the data requested by ViewModels.

