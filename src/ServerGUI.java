import java.rmi.RemoteException;

import java.util.List;

import javax.swing.DefaultListModel;

public class ServerGUI extends javax.swing.JFrame {

   private ServerMain main;

   public ServerGUI(ServerMain main) {
      this.main = main;
      initComponents();
   }

   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {
      jScrollPane1 = new javax.swing.JScrollPane();
      roomsList = new javax.swing.JList<>();
      closeRoomButton = new javax.swing.JButton();

      setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
      setTitle("[Server] - [ChatRMI] T2 - Sistemas Distribuidos");
      setName("qualquer_coisa"); // NOI18N
      setPreferredSize(new java.awt.Dimension(500, 500));
      setResizable(false);

      roomsList.setFont(new java.awt.Font("Meiryo", 0, 14)); // NOI18N
      roomsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
      roomsList.setMaximumSize(new java.awt.Dimension(33, 400));
      roomsList.setMinimumSize(new java.awt.Dimension(33, 400));
      roomsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
         public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
               roomsListValueChanged(evt);
         }
      });
      jScrollPane1.setViewportView(roomsList);

      closeRoomButton.setText("Close");
      closeRoomButton.setEnabled(false);
      closeRoomButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
               closeRoomButtonActionPerformed(evt);
         }
      });

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
               .addContainerGap()
               .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                  .addComponent(closeRoomButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE))
               .addContainerGap())
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
               .addContainerGap()
               .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
               .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
               .addComponent(closeRoomButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addContainerGap())
      );

      pack();
   }// </editor-fold>//GEN-END:initComponents

   private void roomsListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_roomsListValueChanged
      closeRoomButton.setEnabled(true);
   }//GEN-LAST:event_roomsListValueChanged

   private void closeRoomButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeRoomButtonActionPerformed
      String roomName = roomsList.getSelectedValue();
      try {
         main.closeRoom(roomName);
      }
      catch(Exception e) {
         System.out.println("> [ServerGUI] Error: " + e);
         e.printStackTrace();
      }

      roomsList.clearSelection();
      closeRoomButton.setEnabled(false);
   }//GEN-LAST:event_closeRoomButtonActionPerformed

   /*************************
    *        Methods        *
    *************************/
   void attListRooms(List<String> rooms) {
      String value = roomsList.getSelectedValue();
      DefaultListModel listModel = new DefaultListModel();
      for(String r : rooms) {
         listModel.addElement(r);
      }
      roomsList.setModel(listModel);
      if(listModel.contains(value))
         roomsList.setSelectedValue(value, true);
   }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeRoomButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<String> roomsList;
    // End of variables declaration//GEN-END:variables

}
