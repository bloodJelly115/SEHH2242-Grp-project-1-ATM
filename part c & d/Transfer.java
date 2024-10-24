// Transfer.java
// Represents a transfer ATM transaction

public class Transfer extends Transaction
{
   private double amount = 0; // amount to transfer
   private Keypad keypad; // reference to keypad
   private TransferSlot transferSlot; // reference to deposit slot
   private final static int CANCELED = 0; // constant for cancel option
   private boolean checkNumber = false; // check is the account number is valid or invalid

   // Deposit constructor
   public Transfer( int userAccountNumber, Screen atmScreen, 
      BankDatabase atmBankDatabase, Keypad atmKeypad, 
      TransferSlot atmTransferSlot )
   {
      // initialize superclass variables
      super( userAccountNumber, atmScreen, atmBankDatabase );

      // initialize references to keypad and transfer slot
      keypad = atmKeypad;
      transferSlot = atmTransferSlot;
   } // end Deposit constructor

   // perform transaction
   public void execute()
   {
      BankDatabase bankDatabase = getBankDatabase(); // get reference
      Screen screen = getScreen(); // get reference
      
      screen.displayMessage("\nPlease enter the account number you want " +
      "to transfer to: ");
      int accountNumber = keypad.getInput();
      
      checkNumber = bankDatabase.checkAccountNumber( accountNumber );
      if(checkNumber)
      {
          if( accountNumber != getAccountNumber() )
          {
              amount = promptForTransferAmount(); // get transfer amount from user
          }
          else if( accountNumber == getAccountNumber() )
          {
              screen.displayMessageLine( "\nYou cannot transfer to " +
              "same account. So the ATM has canceled your transaction." );
          }
      }
      else if(!checkNumber)
      {
          screen.displayMessageLine( "\nInvalid account number. " +
          "So the ATM has canceled your transaction." );
          amount = CANCELED;
      }
      if ( amount > bankDatabase.getAvailableBalance( getAccountNumber() ) )
      {
          screen.displayMessageLine(
              "\nYou don't have enough money to transfer. " + 
              "So the ATM has canceled your transaction.");
          screen.displayMessageLine( "\nCanceling transaction..." );
      }
      // check whether user entered a transfer amount or canceled
      else if ( amount != CANCELED && 
      amount <= bankDatabase.getAvailableBalance( getAccountNumber() ) )
      {
         // request transfer envelope containing specified amount
         screen.displayMessage( 
            "\nPlease insert a transfer envelope containing " );
         screen.displayDollarAmount( amount );
         screen.displayMessageLine( "." );

         // receive transfer envelope
         boolean envelopeReceived = transferSlot.isEnvelopeReceived();

         // check whether transfer envelope was received
         if ( envelopeReceived )
         {  
            screen.displayMessageLine( "\nYour envelope has been " + 
               "received.\nNOTE: The money just transfered will not " + 
               "be available until we verify the amount of any " +
               "enclosed cash and your checks clear." );
            
            // credit account to reflect the transfer
            bankDatabase.debit( getAccountNumber(), amount ); 
            bankDatabase.credit( accountNumber, amount ); 
         } // end if
         else // transfer envelope not received
         {
            screen.displayMessageLine( "\nYou did not insert an " +
               "envelope, so the ATM has canceled your transaction." );
         } // end else
      } // end if 
      else // user canceled instead of entering amount
      {
         screen.displayMessageLine( "\nCanceling transaction..." );
      } // end else
   } // end method execute

   // prompt user to enter a transfer amount in cents 
   private double promptForTransferAmount()
   {
      Screen screen = getScreen(); // get reference to screen

      // display the prompt
      screen.displayMessage( "\nPlease enter a transfer amount in " + 
         "DOLLARS (or 0 to cancel): " );
      double input = keypad.getMoneyInput(); // receive input of transfer amount
      
      // check whether the user canceled or entered a valid amount
      if ( input == CANCELED ) 
         return CANCELED;
      else
      {
         return ( double ) input; // return dollar amount 
      } // end else
   } // end method promptForTransferAmount
} // end class Transfer



/**************************************************************************
 * (C) Copyright 1992-2007 by Deitel & Associates, Inc. and               *
 * Pearson Education, Inc. All Rights Reserved.                           *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 *************************************************************************/