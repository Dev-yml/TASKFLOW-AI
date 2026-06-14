import Navbar from '../components/landing/Navbar'
import Hero from '../components/landing/Hero'
import Features from '../components/landing/Features'
import Workflow from '../components/landing/Workflow'
import AIShowcase from '../components/landing/AIShowcase'
import Testimonials from '../components/landing/Testimonials'
import Pricing from '../components/landing/Pricing'
import Footer from '../components/landing/Footer'

const Landing = () => {
  return (
    <div className="min-h-screen bg-white dark:bg-[#09090B] antialiased">
      <Navbar />
      <Hero />
      <Features />
      <Workflow />
      <AIShowcase />
      <Testimonials />
      <Pricing />
      <Footer />
    </div>
  )
}

export default Landing
