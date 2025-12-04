import { useEffect, useState } from 'react';
import axios from 'axios';
import './App.css';

function App() {
  const [restaurants, setRestaurants] = useState([]);

  useEffect(() => {
    fetchRestaurants();
  }, []);

  const fetchRestaurants = () => {
    axios.get('http://localhost:8080/api/restaurants')
      .then(response => {
        console.log("Data received:", response.data);
        setRestaurants(response.data);
      })
      .catch(error => {
        console.error("Error fetching data:", error);
      });
  };

  const handleBooking = (tableId) => {
    // 1. Ask for details (Simulating a Login/Datepicker)
    const userId = prompt("Enter your User ID (e.g., 1):");
    if (!userId) return; // Cancelled

    const time = prompt("Enter Time (YYYY-MM-DDTHH:MM:SS):", "2025-12-05T19:00:00");
    if (!time) return; // Cancelled

    // 2. Send the Booking to Java Backend
    axios.post('http://localhost:8080/api/reservations', {
      userId: parseInt(userId),
      tableId: tableId,
      reservationTime: time
    })
    .then(response => {
      alert(`Success :) ! Reservation ID: ${response.data.id}`);
    })
    .catch(error => {
      console.error("Booking Error:", error);
      alert("Booking Failed :( ! (Check console for details)");
    });
  };

  return (
    <div style={{ padding: '20px', fontFamily: 'Arial', maxWidth: '800px', margin: '0 auto' }}>
      <h1 style={{ textAlign: 'center' }}>Restaurant Booking System</h1>

      {restaurants.length === 0 ? (
        <p style={{ textAlign: 'center' }}>Loading restaurants...</p>
      ) : (
        <div style={{ display: 'grid', gap: '20px' }}>
          {restaurants.map(restaurant => (
            <div key={restaurant.id} style={{ border: '1px solid #ddd', padding: '20px', borderRadius: '10px', boxShadow: '0 4px 8px rgba(0,0,0,0.1)' }}>

              {/* Restaurant Details */}
              <h2 style={{ margin: '0 0 10px 0', color: '#333' }}>{restaurant.name}</h2>
              <p style={{ color: '#666', margin: '5px 0' }}>Location: {restaurant.address}</p>
              <p style={{ color: '#666', margin: '5px 0' }}>Call: {restaurant.phoneNumber}</p>

              {/* Table List */}
              <div style={{ marginTop: '20px' }}>
                <h4 style={{ marginBottom: '10px' }}>Available Tables:</h4>

                {(!restaurant.tables || restaurant.tables.length === 0) ? (
                  <p style={{ fontStyle: 'italic', color: '#999' }}>No tables available yet.</p>
                ) : (
                  <div style={{ display: 'flex', gap: '10px', flexWrap: 'wrap' }}>
                    {restaurant.tables.map(table => (
                      <div key={table.id} style={{ border: '1px solid #007bff', borderRadius: '8px', padding: '10px', textAlign: 'center', minWidth: '100px' }}>
                        <p style={{ margin: '0 0 5px 0', fontWeight: 'bold' }}>Table #{table.tableNumber}</p>
                        <p style={{ margin: '0 0 10px 0', fontSize: '0.9em' }}>🪑 {table.seatCount} Seats</p>
                        <button
                          onClick={() => handleBooking(table.id)}
                          style={{ backgroundColor: '#007bff', color: 'white', border: 'none', padding: '5px 10px', borderRadius: '4px', cursor: 'pointer', width: '100%' }}
                        >
                          Book Now
                        </button>
                      </div>
                    ))}
                  </div>
                )}
              </div>

            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default App;
