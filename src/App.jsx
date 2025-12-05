import { useEffect, useState } from 'react';
import axios from 'axios';
import './App.css';

function App() {
  const [restaurants, setRestaurants] = useState([]);
  const [reservations, setReservations] = useState([]);
  const [view, setView] = useState('restaurants');

  // Auth State
  const [govId, setGovId] = useState('');
  const [password, setPassword] = useState('');
  const [user, setUser] = useState(null);
  const [authHeader, setAuthHeader] = useState(null);

  // Registration State
  const [regFirstName, setRegFirstName] = useState('');
  const [regLastName, setRegLastName] = useState('');
  const [regEmail, setRegEmail] = useState('');
  const [regGovId, setRegGovId] = useState('');
  const [regPassword, setRegPassword] = useState('');

  useEffect(() => {
    fetchRestaurants();
  }, []);

  const fetchRestaurants = () => {
    axios.get('https://restaurant-booking-system-v7hc.onrender.com/api/restaurants')
      .then(response => {
        const uniqueRestaurants = response.data.filter((v,i,a)=>a.findIndex(t=>(t.id === v.id))===i);
        setRestaurants(uniqueRestaurants);
      })
      .catch(error => console.error("Error fetching restaurants:", error));
  };

  const handleLogin = (e) => {
    e.preventDefault();
    const token = 'Basic ' + btoa(`${govId}:${password}`);
    const headers = { Authorization: token };

    // Test credentials
    axios.get('https://restaurant-booking-system-v7hc.onrender.com/api/restaurants', { headers })
      .then(() => {
        setAuthHeader(headers);
        const role = govId.includes("ADMIN") ? "ADMIN" : "USER";
        setUser({ govId: govId, role: role });
        setView('restaurants');
      })
      .catch(() => alert("Login Failed: Check Government ID/Password"));
  };

  const handleRegister = (e) => {
    e.preventDefault();
    axios.post('https://restaurant-booking-system-v7hc.onrender.com/api/users', {
      firstName: regFirstName,
      lastName: regLastName,
      email: regEmail,
      governmentId: regGovId,
      password: regPassword,
      role: 'USER'
    })
    .then(() => {
      alert("Registration Successful! Please Login.");
      setView('login');
    })
    .catch(err => alert("Error: " + (err.response?.data?.message || "Gov ID or Email might be taken")));
  };

  const handleBooking = (tableId) => {
    if (!user) {
      alert("Please Login with Gov ID to make a reservation!");
      setView('login');
      return;
    }
    const time = prompt("Enter Time (YYYY-MM-DDTHH:MM:SS):", "2025-12-05T19:00:00");
    if (!time) return;

    axios.post('https://restaurant-booking-system-v7hc.onrender.com/api/reservations', {
      tableId: tableId,
      reservationTime: time
    }, { headers: authHeader })
    .then(response => alert(`Success! Reservation ID: ${response.data.id}`))
    .catch(() => alert("Booking Failed."));
  };

  const fetchReservations = () => {
    axios.get('https://restaurant-booking-system-v7hc.onrender.com/api/reservations', { headers: authHeader })
      .then(response => setReservations(response.data))
      .catch(() => alert("Access Denied"));
  };

  const deleteReservation = (id) => {
    if(!window.confirm("Are you sure you want to delete this reservation?")) return;

    axios.delete(`https://restaurant-booking-system-v7hc.onrender.com/api/reservations/${id}`, { headers: authHeader })
      .then(() => {
        alert("Reservation Deleted");
        fetchReservations(); // Refresh list
      })
      .catch(() => alert("Failed to delete"));
  };

  // Styles
  const containerStyle = { padding: '20px', maxWidth: '800px', margin: '0 auto' };
  const navButtonStyle = { padding: '10px 15px', border: 'none', borderRadius: '5px', cursor: 'pointer', fontSize: '1em' };
  const cardStyle = { border: '1px solid #444', padding: '20px', borderRadius: '10px', marginBottom: '20px', backgroundColor: '#333' };

  return (
    <div style={containerStyle}>
      <nav style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '30px', borderBottom: '1px solid #555', paddingBottom: '15px' }}>
        <h2 style={{ margin: 0 }}>🍽️ Booking System</h2>
        <div style={{ display: 'flex', gap: '10px' }}>
          <button onClick={() => setView('restaurants')} style={{ ...navButtonStyle, backgroundColor: '#444', color: '#fff' }}>Restaurants</button>
          {!user ? (
            <>
              <button onClick={() => setView('login')} style={{ ...navButtonStyle, backgroundColor: '#28a745', color: '#fff' }}>Login</button>
              <button onClick={() => setView('register')} style={{ ...navButtonStyle, backgroundColor: '#17a2b8', color: '#fff' }}>Sign Up</button>
            </>
          ) : (
            <>
              {user.role === 'ADMIN' && (
                <button onClick={() => { setView('admin'); fetchReservations(); }} style={{ ...navButtonStyle, backgroundColor: '#6c757d', color: '#fff' }}>Admin Panel</button>
              )}
              <button onClick={() => { setUser(null); setAuthHeader(null); setView('restaurants'); }} style={{ ...navButtonStyle, backgroundColor: '#dc3545', color: '#fff' }}>Logout</button>
            </>
          )}
        </div>
      </nav>

      {view === 'restaurants' && (
        <div>
          {user && <p style={{marginBottom: '20px', color: '#aaa'}}>Logged in as: {user.govId}</p>}
          {restaurants.map(restaurant => (
            <div key={restaurant.id} style={cardStyle}>
              <h3 style={{ margin: '0 0 10px 0' }}>{restaurant.name}</h3>
              <p style={{ color: '#aaa', margin: '5px 0' }}>📍 {restaurant.address}</p>
              <div style={{ marginTop: '20px', display: 'flex', gap: '10px', flexWrap: 'wrap' }}>
                {restaurant.tables?.map(table => (
                  <button key={table.id} onClick={() => handleBooking(table.id)} style={{ backgroundColor: '#007bff', color: 'white', border: 'none', padding: '10px 15px', borderRadius: '5px', cursor: 'pointer' }}>
                    Table #{table.tableNumber} ({table.seatCount} seats)
                  </button>
                ))}
              </div>
            </div>
          ))}
        </div>
      )}

      {view === 'login' && (
        <div style={{ ...cardStyle, maxWidth: '400px', margin: '50px auto' }}>
          <h3 style={{textAlign: 'center'}}>Login</h3>
          <form onSubmit={handleLogin} style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
            <input type="text" placeholder="Government ID" value={govId} onChange={e => setGovId(e.target.value)} style={{ padding: '10px', borderRadius: '5px', border: '1px solid #555', backgroundColor: '#222', color: '#fff' }} />
            <input type="password" placeholder="Password" value={password} onChange={e => setPassword(e.target.value)} style={{ padding: '10px', borderRadius: '5px', border: '1px solid #555', backgroundColor: '#222', color: '#fff' }} />
            <button type="submit" style={{ ...navButtonStyle, backgroundColor: '#28a745', color: 'white' }}>Sign In</button>
          </form>
          <p style={{textAlign: 'center', fontSize: '0.8em', color: '#aaa', marginTop: '15px'}}>Admin: ADMIN001 / password123</p>
        </div>
      )}

      {view === 'register' && (
        <div style={{ ...cardStyle, maxWidth: '400px', margin: '50px auto' }}>
          <h3 style={{textAlign: 'center'}}>Sign Up</h3>
          <form onSubmit={handleRegister} style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
            <input type="text" placeholder="First Name" value={regFirstName} onChange={e => setRegFirstName(e.target.value)} style={{ padding: '10px', borderRadius: '5px', border: '1px solid #555', backgroundColor: '#222', color: '#fff' }} required />
            <input type="text" placeholder="Last Name" value={regLastName} onChange={e => setRegLastName(e.target.value)} style={{ padding: '10px', borderRadius: '5px', border: '1px solid #555', backgroundColor: '#222', color: '#fff' }} required />
            <input type="email" placeholder="Email" value={regEmail} onChange={e => setRegEmail(e.target.value)} style={{ padding: '10px', borderRadius: '5px', border: '1px solid #555', backgroundColor: '#222', color: '#fff' }} required />
            <input type="text" placeholder="Government ID (Unique)" value={regGovId} onChange={e => setRegGovId(e.target.value)} style={{ padding: '10px', borderRadius: '5px', border: '1px solid #555', backgroundColor: '#222', color: '#fff' }} required />
            <input type="password" placeholder="Password" value={regPassword} onChange={e => setRegPassword(e.target.value)} style={{ padding: '10px', borderRadius: '5px', border: '1px solid #555', backgroundColor: '#222', color: '#fff' }} required />
            <button type="submit" style={{ ...navButtonStyle, backgroundColor: '#17a2b8', color: 'white' }}>Create Account</button>
          </form>
        </div>
      )}

      {view === 'admin' && (
        <div style={cardStyle}>
          <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px'}}>
            <h3 style={{margin: 0}}>Reservations</h3>
            <button onClick={fetchReservations} style={{...navButtonStyle, backgroundColor: '#444', color: '#fff'}}>Refresh</button>
          </div>
          <table style={{ width: '100%', borderCollapse: 'collapse', color: '#fff' }}>
            <thead>
              <tr style={{ borderBottom: '1px solid #555', textAlign: 'left' }}>
                <th style={{ padding: '10px' }}>ID</th>
                <th style={{ padding: '10px' }}>User (GovID)</th>
                <th style={{ padding: '10px' }}>Table</th>
                <th style={{ padding: '10px' }}>Time</th>
                <th style={{ padding: '10px' }}>Action</th>
              </tr>
            </thead>
            <tbody>
              {reservations.map(res => (
                <tr key={res.id} style={{ borderBottom: '1px solid #444' }}>
                  <td style={{ padding: '10px' }}>{res.id}</td>
                  <td style={{ padding: '10px' }}>{res.user?.firstName} ({res.user?.governmentId})</td>
                  <td style={{ padding: '10px' }}>Table #{res.table?.tableNumber}</td>
                  <td style={{ padding: '10px' }}>{new Date(res.reservationTime).toLocaleString()}</td>
                  <td style={{ padding: '10px' }}>
                    <button onClick={() => deleteReservation(res.id)} style={{ padding: '5px 10px', backgroundColor: '#dc3545', color: 'white', border: 'none', borderRadius: '3px', cursor: 'pointer' }}>Delete</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default App;
