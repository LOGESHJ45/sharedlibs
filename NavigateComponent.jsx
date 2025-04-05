import React from 'react'
import { Routes, Link, Route, BrowserRouter as Router } from 'react-router-dom'


import StateDemo from './StateDemo/StateDemo'

const NavigateComponent = () => {
    
  return (
    <div>
    <Router>
        <nav>
            <ul>
                <li><Link to='/'>Hello Function</Link></li>
                
                <li><Link to='/State'>Hello State Function</Link></li>
                <li><Link to='/Function'>Hello Functional Function</Link></li>
            </ul>
        </nav>
    
        <Routes>
            <Route path = '/' element={<HelloCompo/>}/>
            
            <Route path = '/State' element={<StateDemo/>}/>
            <Route path = '/Function' element={<HelloCompo/>}/>
        </Routes>
    </Router>
    </div>
    
  

  )
  
}

export default NavigateComponent